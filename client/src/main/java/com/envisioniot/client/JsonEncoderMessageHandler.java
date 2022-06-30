package com.envisioniot.client;

import com.envisioniot.payload.JsonCodec;
import com.envisioniot.payload.json.JsonUploadMeasurepoint;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * Desc:
 *
 * @author zhonghua.wu
 * @date 2022-06-30 15:07:08
 */
public class JsonEncoderMessageHandler extends ChannelOutboundHandlerAdapter {
    private final JsonCodec jsonCodec = new JsonCodec();

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        Channel channel = ctx.channel();
        JsonUploadMeasurepoint jsonUploadMeasurepoint = (JsonUploadMeasurepoint) msg;
        byte[] bytes = jsonCodec.encode(jsonUploadMeasurepoint);
        ctx.write(msg, promise);
    }

}
