package com.envisioniot.transport.pipeline;

import io.netty.channel.ChannelPipeline;

@FunctionalInterface
public interface PipelineInitializer {
    void init(ChannelPipeline pipeline) throws Exception;
}
