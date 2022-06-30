package com.envisioniot.client;

import com.envisioniot.payload.JsonCodec;
import com.envisioniot.payload.json.JsonUploadMeasurepoint;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * Desc:
 *
 * @author zhonghua.wu
 * @date 2022-06-30 15:07:08
 */
public class JsonEncoderMessageHandler extends MessageToMessageEncoder<JsonUploadMeasurepoint> {
    private final JsonCodec jsonCodec = new JsonCodec();

    @Override
    protected void encode(ChannelHandlerContext ctx, JsonUploadMeasurepoint msg, List<Object> out) throws Exception {
        out.add(Unpooled.wrappedBuffer(jsonCodec.encode(msg)));
    }
}
