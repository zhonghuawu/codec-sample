package com.envisioniot.client;

import com.envisioniot.payload.cbor.CborCodec;
import com.envisioniot.payload.cbor.CborUploadMeasurepoint;
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
public class CborEncoderMessageHandler extends MessageToMessageEncoder<CborUploadMeasurepoint> {
    private final CborCodec cborCodec = new CborCodec();

    @Override
    protected void encode(ChannelHandlerContext ctx, CborUploadMeasurepoint msg, List<Object> out) throws Exception {
        out.add(Unpooled.wrappedBuffer(cborCodec.encode(msg)));
    }
}
