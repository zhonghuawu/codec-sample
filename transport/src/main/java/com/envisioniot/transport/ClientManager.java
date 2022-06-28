package com.envisioniot.transport;

import com.envisioniot.transport.context.ClientNettyContext;
import com.envisioniot.transport.context.NettyContextResolver;
import com.envisioniot.transport.pipeline.PipelineInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class that manages all local client channels.
 *
 * @author jian.zhang4
 */
@Slf4j
public class ClientManager extends ChannelManager<Channel, ClientNettyContext> {
    private static final AttributeKey<Endpoint> ATTR_ENDPOINT = AttributeKey.valueOf("ENDPOINT");

    private final Map<Endpoint, Channel> clients = new ConcurrentHashMap<>();
    private final Map<Endpoint, ChannelFuture> pendingClients = new ConcurrentHashMap<>();
    private final Bootstrap bootstrap;

    public ClientManager(int nThreads, final PipelineInitializer pipelineInit) {
        this(nThreads, 5000, pipelineInit);
    }

    public ClientManager(int nThreads, int connTimeoutMillis, final PipelineInitializer pipelineInit) {
        this(NettyContextResolver.createClientContext(nThreads), connTimeoutMillis, pipelineInit);
    }

    public ClientManager(ClientNettyContext context, int connTimeoutMillis, final PipelineInitializer pipelineInit) {
        super(context);

        bootstrap = new Bootstrap()
                .group(context.getLoopGroup())
                .channel(context.getChannelClass())
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        pipelineInit.init(ch.pipeline());

                        ch.pipeline().addFirst(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                Endpoint endpoint = ctx.channel().attr(ATTR_ENDPOINT).get();

                                // This could happen if we re-use the existing channel.
                                if (endpoint != null) {
                                    log.debug("Endpoint {} became inactive", endpoint);
                                    // be careful that the closing channel could have be replaced by other new channel
                                    clients.remove(endpoint, ctx.channel());
                                }

                                // delegate to super class
                                super.channelInactive(ctx);
                            }
                        });
                    }
                })
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connTimeoutMillis);
    }

    public void shutdown() {
        clients.values().forEach(channel -> {
            try {
                channel.close().sync();
            } catch (Exception e) {
                // ignore this
            }
        });
        context.shutdown();
    }

    public void closeEndpoint(Endpoint address) {
        Channel channel = clients.remove(address);
        if (channel == null) {
            log.debug("failed to close endpoint {} as it doesn't exist", address);
        } else {
            try {
                channel.close().sync();
            } catch (Throwable error) {
                // ignore this
            }
        }
    }

    public Promise<Boolean> sendMessage(Endpoint address, final Object data) {
        final DefaultPromise<Boolean> promise = new DefaultPromise<>(context.getLoopGroup().next());

        Channel channel = clients.get(address);
        if (channel == null) {
            ChannelFuture future = pendingClients.computeIfAbsent(address, this::createChannel);
            future.addListener((ChannelFutureListener) connFuture -> {
                // Set up the channel for the end-point first
                setupEndpointChannel(connFuture, address);

                Channel newChannel = clients.get(address);
                if (newChannel == null) {
                    if (connFuture.cause() == null) {
                        promise.setFailure(new IllegalStateException("[BUG] failure cause expected"));
                    } else {
                        promise.setFailure(connFuture.cause());
                        connFuture.channel().close();
                    }
                } else {
                    doSendMessage(newChannel, data, promise);
                }
            });
        } else {
            doSendMessage(channel, data, promise);
        }

        return promise;
    }

    private void doSendMessage(Channel channel, Object data, final DefaultPromise<Boolean> promise) {
        channel.writeAndFlush(data).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture sendFuture) {
                if (sendFuture.cause() != null) {
                    promise.setFailure(sendFuture.cause());
                    sendFuture.channel().close();
                } else {
                    promise.setSuccess(Boolean.TRUE);
                }
            }
        });
    }

    /**
     * Be careful about the logic here. It's easy to introduce bugs here.
     * <p>
     * If you want to make any changes here, think twice and ask for others' comments.
     *
     * @param connFuture
     * @param address
     */
    private synchronized void setupEndpointChannel(ChannelFuture connFuture, Endpoint address) {
        Channel seenChannel = clients.get(address);

        if (connFuture.cause() == null) {
            if (seenChannel != null && seenChannel != connFuture.channel()) {
                // We should re-use the existing channel and close this newly created channel
                connFuture.channel().close();
            }
        }

        // bind the channel if this is the first channel we see for the endpoint
        if (seenChannel == null && connFuture.cause() == null) {
            log.debug("bind new channel {} to {}", connFuture.channel(), address);
            connFuture.channel().attr(ATTR_ENDPOINT).set(address);
            clients.put(address, connFuture.channel());
        }

        // We need to handle this after we populate clients
        pendingClients.remove(address);
    }

    ChannelFuture createChannel(final Endpoint address) {
        return bootstrap.connect(new InetSocketAddress(address.getHost(), address.getPort()));
    }

    public Channel getChannel(final Endpoint address) {
        return clients.get(address);
    }

}
