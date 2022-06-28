package com.envisioniot.transport.context;

import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class NettyContext<T extends Channel> {

    protected final EventLoopGroup loopGroup;
    protected final Class<T> channelClass;

    public NettyContext(EventLoopGroup loopGroup, Class<T> channelClass) {
        this.loopGroup = loopGroup;
        this.channelClass = channelClass;
    }

    public EventLoopGroup getLoopGroup() {
        return loopGroup;
    }

    public Class<T> getChannelClass() {
        return channelClass;
    }

    public void shutdown() {
        shutdownLoopGroup(getLoopGroup());
    }

    protected void shutdownLoopGroup(EventLoopGroup group) {
        Future<?> workerWaiter = group.shutdownGracefully();

        try {
            workerWaiter.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException iex) {
            log.warn("An InterruptedException was caught while waiting for event loops to terminate...");
        }

        if (!group.isTerminated()) {
            log.warn("Forcing shutdown of worker event loop...");
            group.shutdownGracefully(0L, 0L, TimeUnit.MILLISECONDS);
        }
    }
}
