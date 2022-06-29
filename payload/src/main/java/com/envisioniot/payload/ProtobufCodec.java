package com.envisioniot.payload;

import com.envisioniot.payload.proto.ProtoUploadMeasurepointProto;
import lombok.SneakyThrows;

/**
 * Desc:
 *
 * @author zhonghua.wu
 * @date 2022-06-29 00:26:23
 */
public class ProtobufCodec implements ICodec<ProtoUploadMeasurepointProto.UploadMeasurepoint> {
    @Override
    public byte[] encode(ProtoUploadMeasurepointProto.UploadMeasurepoint uploadMeasurepoint) {
        return uploadMeasurepoint.toByteArray();
    }

    @SneakyThrows
    @Override
    public ProtoUploadMeasurepointProto.UploadMeasurepoint decode(byte[] bytes) {
        return ProtoUploadMeasurepointProto.UploadMeasurepoint.parseFrom(bytes);
    }
}
