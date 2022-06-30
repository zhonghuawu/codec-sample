package com.envisioniot.server.netty;

import com.envisioniot.payload.json.JsonCodec;
import com.envisioniot.payload.json.JsonUploadMeasurepoint;
import io.netty.buffer.ByteBuf;
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
public class JsonDecoderMessageHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private final JsonCodec jsonCodec = new JsonCodec();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        log.info("received jsonUploadMeasurepoint body[{}]", byteBuf);
        int readableBytes = byteBuf.readableBytes();
        byte[] bytes = new byte[readableBytes];
        byteBuf.readBytes(bytes);
        log.info("receive jsonUploadMeasurepoint string[{}]", new String(bytes));
        JsonUploadMeasurepoint jsonUploadMeasurepoint = jsonCodec.decode(bytes);
        log.info("receive jsonUploadMeasurepoint[{}]", jsonUploadMeasurepoint);
    }

}
