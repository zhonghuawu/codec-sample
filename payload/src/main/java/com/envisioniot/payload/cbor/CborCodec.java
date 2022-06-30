package com.envisioniot.payload.cbor;

import com.envisioniot.payload.ICodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.cbor.databind.CBORMapper;
import lombok.SneakyThrows;

import java.util.*;

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
        CborUploadMeasurepoint cborUploadMeasurepoint = new CborUploadMeasurepoint();
        JsonNode root = mapper.readTree(bytes);
        // todo: check root is array
        ArrayNode rootArrayNode = (ArrayNode) root;
        // todo: check root length is 4
        cborUploadMeasurepoint.setId(rootArrayNode.get(0).asText());
        cborUploadMeasurepoint.setVersion(rootArrayNode.get(1).asText());
        cborUploadMeasurepoint.setMethod(root.get(2).asText());
        ArrayNode paramsArrayNode = (ArrayNode) rootArrayNode.get(3);
        CborUploadMeasurepoint.Params params = new CborUploadMeasurepoint.Params();
        // todo: check params is array, length = 2
        params.setTimestamp(paramsArrayNode.get(0).asLong());
        ObjectNode paramsMapNode = (ObjectNode) paramsArrayNode.get(1);
        // todo: check params node is array
        Map<String, Object> measurepoint = new HashMap<>();
        for (Iterator<Map.Entry<String, JsonNode>> it = paramsMapNode.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = it.next();
            String key = entry.getKey();
            JsonNode valueNode = entry.getValue();
            Object value = null;
            switch (valueNode.getNodeType()) {
                case ARRAY:
                    ArrayNode valueArrayNode = (ArrayNode) valueNode;
                    if (valueArrayNode.get(0).getNodeType() == JsonNodeType.STRING) {
                        List<String> list = new ArrayList<>();
                        valueArrayNode.forEach(n -> list.add(n.asText()));
                        value = list;
                    } else if (valueArrayNode.get(0).getNodeType() == JsonNodeType.NUMBER) {
                        List<Double> list = new ArrayList<>();
                        valueArrayNode.forEach(n -> list.add(n.asDouble()));
                        value = list;
                    }
                    break;
                case BINARY:
                case BOOLEAN:
                case MISSING:
                case NULL:
                case NUMBER:
                    value = valueNode.asDouble();
                    break;
                case OBJECT:
                case POJO:
                case STRING:
                    value = valueNode.asText();
                    break;
                default:
            }
            measurepoint.put(key, value);
        }
        params.setMeasurepoints(measurepoint);
        cborUploadMeasurepoint.setParams(params);
        return cborUploadMeasurepoint;
    }
}
