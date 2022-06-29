package com.envisioniot.payload;

import com.envisioniot.payload.cbor.CborUploadMeasurepoint;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.cbor.databind.CBORMapper;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Map;

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
        ArrayNode root = mapper.createArrayNode();
        root.add(cborUploadMeasurepoint.getId());
        root.add(cborUploadMeasurepoint.getVersion());
        root.add(cborUploadMeasurepoint.getMethod());

        ArrayNode paramsNode = mapper.createArrayNode();
        paramsNode.add(cborUploadMeasurepoint.getParams().getTimestamp());

        ObjectNode measurepointsNode = mapper.createObjectNode();
        for (Map.Entry<String, Object> entry : cborUploadMeasurepoint.getParams().getMeasurepoints().entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Integer) {
                measurepointsNode.put(key, (Integer) value);
            } else if (value instanceof Long) {
                measurepointsNode.put(key, (Long) value);
            } else if (value instanceof Float) {
                measurepointsNode.put(key, (Float) value);
            } else if (value instanceof Double) {
                measurepointsNode.put(key, (Double) value);
            } else if (value instanceof String) {
                measurepointsNode.put(key, (String) value);
            } else if (value instanceof List) {
                List<?> valueList = (List<?>) value;
                ArrayNode innerNode = measurepointsNode.putArray(key);
                for (Object innerValue : valueList) {
                    if (innerValue instanceof Integer) {
                        innerNode.add((Integer) innerValue);
                    } else if (innerValue instanceof Float) {
                        innerNode.add((Float) innerValue);
                    } else if (innerValue instanceof Double) {
                        innerNode.add((Double) innerValue);
                    } else {
                        innerNode.add(innerValue.toString());
                    }
                }
            } else if (value instanceof Map) {
                Map<?, ?> mapNode = (Map<?, ?>) value;
                ObjectNode innerNode = measurepointsNode.putObject(key);
                for (Map.Entry<?, ?> innerEntry : mapNode.entrySet()) {
                    String innerKey = (String) innerEntry.getKey();
                    Object innerValue = innerEntry.getValue();
                    if (innerValue instanceof Integer) {
                        innerNode.put(innerKey, (Integer) innerValue);
                    } else if (innerValue instanceof Float) {
                        innerNode.put(innerKey, (Float) innerValue);
                    } else if (innerValue instanceof Double) {
                        innerNode.put(innerKey, (Double) innerValue);
                    } else {
                        innerNode.put(innerKey, innerValue.toString());
                    }
                }
            }
        }

        paramsNode.add(measurepointsNode);
        root.add(paramsNode);
        return mapper.writeValueAsBytes(root);
    }

    @SneakyThrows
    @Override
    public CborUploadMeasurepoint decode(byte[] bytes) {
        return mapper.readValue(bytes, CborUploadMeasurepoint.class);
    }
}
