package com.envisioniot.payload.json;

import com.envisioniot.payload.ICodec;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

/**
 * Desc:
 *
 * @author zhonghua.wu
 * @date 2022-06-29 00:30:52
 */
public class JsonCodec implements ICodec<JsonUploadMeasurepoint> {

    private final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    @Override
    public byte[] encode(JsonUploadMeasurepoint jsonUploadMeasurepoint) {
        return mapper.writeValueAsBytes(jsonUploadMeasurepoint);
    }

    @SneakyThrows
    @Override
    public JsonUploadMeasurepoint decode(byte[] bytes) {
        return mapper.readValue(bytes, JsonUploadMeasurepoint.class);
    }
}
