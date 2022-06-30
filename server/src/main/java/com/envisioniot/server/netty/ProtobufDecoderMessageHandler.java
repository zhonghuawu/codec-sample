package com.envisioniot.server.netty;

import com.envisioniot.payload.proto.ProtoUploadMeasurepointFixed;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Desc:
 *
 * @author zhonghua.wu
 * @date 2022-06-26 23:33:26
 */
@Slf4j
public class ProtobufDecoderMessageHandler extends SimpleChannelInboundHandler<ProtoUploadMeasurepointFixed.UploadMeasurepoint> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ProtoUploadMeasurepointFixed.UploadMeasurepoint o) throws Exception {
        log.info("received protobufUploadMeasurepoint[{}]", o);
    }

}
