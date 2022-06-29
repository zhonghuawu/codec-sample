package com.envisioniot.server.netty;

import com.envisioniot.payload.proto.ProtoUploadMeasurepoint;
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
public class MessageHandler extends SimpleChannelInboundHandler<ProtoUploadMeasurepoint.UploadMeasurepoint> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ProtoUploadMeasurepoint.UploadMeasurepoint o) throws Exception {
        log.info("received measurepoint body[{}]", o.toString());
    }

}
