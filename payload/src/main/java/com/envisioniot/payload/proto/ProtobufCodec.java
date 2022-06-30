package com.envisioniot.payload.proto;

import com.envisioniot.payload.ICodec;
import com.envisioniot.payload.proto.ProtoUploadMeasurepoint;
import lombok.SneakyThrows;

/**
 * Desc:
 *
 * @author zhonghua.wu
 * @date 2022-06-29 00:26:23
 */
public class ProtobufCodec implements ICodec<ProtoUploadMeasurepoint.UploadMeasurepoint> {
    @Override
    public byte[] encode(ProtoUploadMeasurepoint.UploadMeasurepoint uploadMeasurepoint) {
        return uploadMeasurepoint.toByteArray();
    }

    @SneakyThrows
    @Override
    public ProtoUploadMeasurepoint.UploadMeasurepoint decode(byte[] bytes) {
        return ProtoUploadMeasurepoint.UploadMeasurepoint.parseFrom(bytes);
    }
}
