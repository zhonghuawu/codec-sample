package com.envisioniot.transport.pipeline;

import com.google.protobuf.MessageLite;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelPipeline;

import java.util.function.Supplier;

public class ProtoBufRWPipelineInitializer<T extends ChannelInboundHandler> implements PipelineInitializer {
    private final ProtoBufReadPipelineInitializer<T> readPipeInitializer;
    private final ProtoBufWritePipelineInitializer writePipeInitializer;

    public ProtoBufRWPipelineInitializer(MessageLite messageLite, Supplier<T> bizHandler) {
        readPipeInitializer = new ProtoBufReadPipelineInitializer<>(messageLite, bizHandler);
        writePipeInitializer = new ProtoBufWritePipelineInitializer();
    }

    @Override
    public void init(ChannelPipeline pipeline) throws Exception {
        readPipeInitializer.init(pipeline);
        writePipeInitializer.init(pipeline);
    }

}
