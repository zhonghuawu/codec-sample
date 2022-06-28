package com.envisioniot.payload;

import com.envisioniot.payload.cbor.CborUploadMeasurepoint;
import com.fasterxml.jackson.dataformat.cbor.databind.CBORMapper;
import lombok.SneakyThrows;

/**
 * Desc:
 *
 * @author zhonghua.wu
 * @date 2022-06-29 00:34:31
 */
public class CborCodec implements ICodec<CborUploadMeasurepoint> {

    private final CBORMapper mapper = new CBORMapper();

    @SneakyThrows
    @Override
    public byte[] encode(CborUploadMeasurepoint cborUploadMeasurepoint) {
        return mapper.writeValueAsBytes(cborUploadMeasurepoint);
    }

    @SneakyThrows
    @Override
    public CborUploadMeasurepoint decode(byte[] bytes) {
        return mapper.readValue(bytes, CborUploadMeasurepoint.class);
    }
}
