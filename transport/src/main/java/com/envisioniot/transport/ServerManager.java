package com.envisioniot.transport;

import com.envisioniot.transport.context.NettyContextResolver;
import com.envisioniot.transport.context.ServerNettyContext;
import com.envisioniot.transport.pipeline.PipelineInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerManager extends ChannelManager<ServerChannel, ServerNettyContext> {
    private final ServerBootstrap bootstrap;
    private final int port;
    private volatile Channel serverChannel;

    /**
     * Use the same event loop group for both boss and worker. And the group
     * would have default number of threads assigned (2 * cores).
     * @param port
     * @param childPipelineInit
     */
    public ServerManager(int port, final PipelineInitializer childPipelineInit) {
        this(port, 0, -1, childPipelineInit);
    }

    public ServerManager(int port, int bossThreads, int workerThreads, final PipelineInitializer childPipelineInit) {
        this(port, NettyContextResolver.createServerContext(bossThreads, workerThreads), childPipelineInit);
    }

    public ServerManager(int port, ServerNettyContext context, final PipelineInitializer childPipelineInit) {
        super(context);

        this.bootstrap = new ServerBootstrap()
                .group(context.getBossGroup(), context.getWorkerGroup())
                .channel(context.getChannelClass())
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        log.debug("now init a new accepted channel {}", ch);

                        childPipelineInit.init(ch.pipeline());

                        // Do logging when a client disconnects
                        ch.pipeline().addFirst(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                log.debug("client channel {} became inactive", ch);
                                super.channelInactive(ctx);
                            }
                        });
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 512)
                .option(ChannelOption.SO_REUSEADDR, true);
        this.port = port;
    }

    public ChannelFuture start() {
        ChannelFuture future = bootstrap.bind(port);
        future.addListener((ChannelFutureListener) listener -> {
            if (listener.cause() == null) {
                serverChannel = listener.channel();
            }
        });
        return future;
    }

    public void shutdown() {
        if (serverChannel != null) {
            try {
                serverChannel.close().sync();
            } catch (Exception e) {
                // ignore this
            }
        }
        context.shutdown();
    }
}
