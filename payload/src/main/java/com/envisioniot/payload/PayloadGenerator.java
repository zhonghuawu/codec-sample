package com.envisioniot.payload;

import com.envisioniot.payload.cbor.CborUploadMeasurepoint;
import com.envisioniot.payload.json.JsonUploadMeasurepoint;
import com.envisioniot.payload.proto.ProtoUploadMeasurepoint;
import com.envisioniot.payload.proto.ProtoUploadMeasurepointFixed;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.protobuf.*;
import org.springframework.data.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Desc:
 *
 * @author zhonghua.wu
 * @date 2022-06-29 13:47:58
 */
public class PayloadGenerator {

    private static final String id = "1";
    private static final String version = "1.0";
    private static final String method = "thing.measurepoint.port";

    private static final long timestamp = System.currentTimeMillis();

    public static JsonUploadMeasurepoint json() {
        JsonUploadMeasurepoint.Params params = new JsonUploadMeasurepoint.Params();
        params.setTimestamp(timestamp);
        params.setMeasurepoints(measurepoints());

        JsonUploadMeasurepoint jsonUploadMeasurepoint = new JsonUploadMeasurepoint();
        jsonUploadMeasurepoint.setId(id);
        jsonUploadMeasurepoint.setVersion(version);
        jsonUploadMeasurepoint.setMethod(method);
        jsonUploadMeasurepoint.setParams(params);
        return jsonUploadMeasurepoint;
    }

    public static CborUploadMeasurepoint cbor() {
        CborUploadMeasurepoint.Params params = new CborUploadMeasurepoint.Params();
        params.setTimestamp(timestamp);
        params.setMeasurepoints(measurepoints());

        CborUploadMeasurepoint cborUploadMeasurepoint = new CborUploadMeasurepoint();
        cborUploadMeasurepoint.setId(id);
        cborUploadMeasurepoint.setVersion(version);
        cborUploadMeasurepoint.setMethod(method);
        cborUploadMeasurepoint.setParams(params);
        return cborUploadMeasurepoint;
    }

    public static ProtoUploadMeasurepoint.UploadMeasurepoint protobuf() {
        return ProtoUploadMeasurepoint.UploadMeasurepoint.newBuilder()
                .setId(id)
                .setVersion(version)
                .setMethod(method)
                .setParams(ProtoUploadMeasurepoint.Params.newBuilder()
                        .setTimestamp(timestamp)
                        .addMeasurepoint(ProtoUploadMeasurepoint.Measurepoint.newBuilder()
                                .setKey("mp_int").setValue(Any.pack(Int32Value.of(mp_int)))
                                .build())
                        .addMeasurepoint(ProtoUploadMeasurepoint.Measurepoint.newBuilder()
                                .setKey("mp_long").setValue(Any.pack(Int64Value.of(mp_long)))
                                .build())
                        .addMeasurepoint(ProtoUploadMeasurepoint.Measurepoint.newBuilder()
                                .setKey("mp_float").setValue(Any.pack(FloatValue.of(mp_float)))
                                .build())
                        .addMeasurepoint(ProtoUploadMeasurepoint.Measurepoint.newBuilder()
                                .setKey("mp_double").setValue(Any.pack(DoubleValue.of(mp_double)))
                                .build())
                        .addMeasurepoint(ProtoUploadMeasurepoint.Measurepoint.newBuilder()
                                .setKey("mp_enum").setValue(Any.pack(StringValue.of(mp_string)))
                                .build())
                        .addMeasurepoint(ProtoUploadMeasurepoint.Measurepoint.newBuilder()
                                .setKey("mp_timestamp").setValue(Any.pack(UInt64Value.of(mp_timestamp)))
                                .build())
                        .addMeasurepoint(ProtoUploadMeasurepoint.Measurepoint.newBuilder()
                                .setKey("mp_date").setValue(Any.pack(StringValue.of(mp_date)))
                                .build())
                        .addMeasurepoint(ProtoUploadMeasurepoint.Measurepoint.newBuilder()
                                .setKey("mp_array_int").setValue(Any.pack(ListValue.newBuilder()
                                        .addAllValues(mp_array_int.stream()
                                                .map(num -> Value.newBuilder().setNumberValue(num).build())
                                                .collect(Collectors.toList()))
                                        .build()))
                                .build())
                        .addMeasurepoint(ProtoUploadMeasurepoint.Measurepoint.newBuilder()
                                .setKey("mp_array_float").setValue(Any.pack(ListValue.newBuilder()
                                        .addAllValues(mp_array_float.stream()
                                                .map(num -> Value.newBuilder().setNumberValue(num).build())
                                                .collect(Collectors.toList()))
                                        .build()))
                                .build())
                        .addMeasurepoint(ProtoUploadMeasurepoint.Measurepoint.newBuilder()
                                .setKey("mp_array_double").setValue(Any.pack(ListValue.newBuilder()
                                        .addAllValues(mp_array_double.stream()
                                                .map(num -> Value.newBuilder().setNumberValue(num).build())
                                                .collect(Collectors.toList()))
                                        .build()))
                                .build())
                        .addMeasurepoint(ProtoUploadMeasurepoint.Measurepoint.newBuilder()
                                .setKey("mp_array_string").setValue(Any.pack(ListValue.newBuilder()
                                        .addAllValues(mp_array_string.stream()
                                                .map(s -> Value.newBuilder().setStringValue(s).build())
                                                .collect(Collectors.toList()))
                                        .build()))
                                .build())
                        .addMeasurepoint(ProtoUploadMeasurepoint.Measurepoint.newBuilder()
                                .setKey("mp_struct").setValue(Any.pack(Struct.newBuilder()
                                        .putAllFields(mp_struct.entrySet().stream()
                                                .map(entry -> {
                                                    if (entry.getValue() instanceof Number) {
                                                        return Pair.of(entry.getKey(), Value.newBuilder()
                                                                .setNumberValue(((Number) entry.getValue()).doubleValue())
                                                                .build());
                                                    } else {
                                                        return Pair.of(entry.getKey(), Value.newBuilder()
                                                                .setStringValue(entry.getValue().toString())
                                                                .build());
                                                    }
                                                }).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond)))
                                        .build()))
                                .build())
                )
                .build();
    }


    public static ProtoUploadMeasurepointFixed.UploadMeasurepoint protobufFixed() {
        return ProtoUploadMeasurepointFixed.UploadMeasurepoint.newBuilder()
                .setId(id)
                .setVersion(version)
                .setMethod(method)
                .setParams(ProtoUploadMeasurepointFixed.Params.newBuilder()
                        .setTimestamp(timestamp)
                        .setMeasurepoint(ProtoUploadMeasurepointFixed.Measurepoint.newBuilder()
                                .setMpInt(mp_int)
                                .setMpLong(mp_long)
                                .setMpFloat(mp_float)
                                .setMpDouble(mp_double)
                                .setMpEnumValue(0)
                                .setMpString(mp_string)
                                .setMpTimestamp(mp_timestamp)
                                .setMpDate(mp_date)
                                .addAllMpArrayInt(mp_array_int)
                                .addAllMpArrayFloat(mp_array_float)
                                .addAllMpArrayDouble(mp_array_double)
                                .addAllMpArrayString(mp_array_string)
                                .setMpStruct(ProtoUploadMeasurepointFixed.Measurepoint.MpStruct.newBuilder()
                                        .setMpStructInt(mp_struct_int)
                                        .setMpStructString(mp_struct_string)
                                        .build())
                        )
                )
                .build();
    }

    public static Map<String, Object> measurepoints() {
        Map<String, Object> measurepoint = new HashMap<>();
        measurepoint.put("mp_int", mp_int);
        measurepoint.put("mp_long", mp_long);
        measurepoint.put("mp_float", mp_float);
        measurepoint.put("mp_double", mp_double);
        measurepoint.put("mp_enum", mp_enum);
        measurepoint.put("mp_string", mp_string);
        measurepoint.put("mp_timestamp", mp_timestamp);
        measurepoint.put("mp_date", mp_date);
        measurepoint.put("mp_array_int", mp_array_int);
        measurepoint.put("mp_array_float", mp_array_float);
        measurepoint.put("mp_array_double", mp_array_double);
        measurepoint.put("mp_array_string", mp_array_string);
        measurepoint.put("mp_struct", mp_struct);
        return measurepoint;
    }

    public static final int mp_int = 1;
    public static final long mp_long = 11;
    public static final float mp_float = 1.1f;
    public static final double mp_double = 1.11d;
    public static final String mp_enum = "enum_1";
    public static final String mp_string = "string_1";
    public static final long mp_timestamp = 1656483375120L;
    public static final String mp_date = "2022-07-01";

    public static final List<Integer> mp_array_int = Lists.newArrayList(1, 2, 3, 4);
    public static final List<Float> mp_array_float = Lists.newArrayList(1.1f, 2.2f, 3.3f, 4.4f);
    public static final List<Double> mp_array_double = Lists.newArrayList(11.11d, 22.22d, 33.33d, 44.44d);
    public static final List<String> mp_array_string = Lists.newArrayList("aa", "bb", "cc", "dd");

    public static final Map<String, Object> mp_struct = Maps.newHashMap();
    public static final int mp_struct_int = 1;
    public static final String mp_struct_string = "inner_abcd";

    static {
        mp_struct.put("mp_struct_int", mp_struct_int);
        mp_struct.put("mp_struct_string", mp_struct_string);
    }

}
