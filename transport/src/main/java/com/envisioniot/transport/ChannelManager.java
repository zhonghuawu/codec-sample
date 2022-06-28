package com.envisioniot.transport;

import com.envisioniot.transport.context.NettyContext;
import io.netty.channel.Channel;

public abstract class ChannelManager<T extends Channel, C extends NettyContext<T>> {
    protected final C context;

    public ChannelManager(C context) {
        this.context = context;
    }

    public C getContext() {
        return context;
    }
}
