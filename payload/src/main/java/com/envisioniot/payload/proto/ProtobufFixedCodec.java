package com.envisioniot.payload.proto;

import com.envisioniot.payload.ICodec;
import com.envisioniot.payload.proto.ProtoUploadMeasurepointFixed;
import lombok.SneakyThrows;

/**
 * Desc:
 *
 * @author zhonghua.wu
 * @date 2022-06-29 00:26:23
 */
public class ProtobufFixedCodec implements ICodec<ProtoUploadMeasurepointFixed.UploadMeasurepoint> {
    @Override
    public byte[] encode(ProtoUploadMeasurepointFixed.UploadMeasurepoint uploadMeasurepoint) {
        return uploadMeasurepoint.toByteArray();
    }

    @SneakyThrows
    @Override
    public ProtoUploadMeasurepointFixed.UploadMeasurepoint decode(byte[] bytes) {
        return ProtoUploadMeasurepointFixed.UploadMeasurepoint.parseFrom(bytes);
    }
}
