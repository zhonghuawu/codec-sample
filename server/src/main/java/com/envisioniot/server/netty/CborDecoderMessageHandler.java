package com.envisioniot.server.netty;

import com.envisioniot.payload.cbor.CborCodec;
import com.envisioniot.payload.cbor.CborUploadMeasurepoint;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

/**
 * Desc:
 *
 * @author zhonghua.wu
 * @date 2022-06-26 23:33:26
 */
@Slf4j
public class CborDecoderMessageHandler extends SimpleChannelInboundHandler<byte[]> {

    private final CborCodec cborCodec = new CborCodec();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, byte[] bytes) throws Exception {
        log.info("received cborUploadMeasurepoint body[{}]", Hex.encodeHexString(bytes));
        CborUploadMeasurepoint cborUploadMeasurepoint = cborCodec.decode(bytes);
        log.info("received cborUploadMeasurepoint[{}]", cborUploadMeasurepoint);
    }

}
