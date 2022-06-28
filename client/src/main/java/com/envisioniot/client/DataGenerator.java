package com.envisioniot.client;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * Desc:
 *
 * @author zhonghua.wu
 * @date 2022-06-29 00:03:08
 */
public class DataGenerator {

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

    static {
        mp_struct.put("mp_struct_int", 1.0);
        mp_struct.put("mp_struct_string", "inner_abcd");
    }

}
