package com.envisioniot.payload;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.gson.Gson;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.Serializable;

/**
 * Desc:
 *
 * @author zhonghua.wu
 * @date 2022-06-29 13:56:45
 */
public class JsonTest {
    @SneakyThrows
    public static void main(String[] args) {
        FileBlock fileBlock = new FileBlock();
        fileBlock.index = 2;
        byte[] bytes = "abcdefghijk".getBytes();
        fileBlock.block = bytes;
        System.out.println(fileBlock);

        JsonMapper mapper = new JsonMapper();
        String encodeJackson = mapper.writeValueAsString(fileBlock);
        System.out.println("size: " + encodeJackson.length() + ", " + encodeJackson);

        FileBlock decoded = mapper.readValue(encodeJackson, FileBlock.class);
        System.out.println(decoded);

        Gson gson = new Gson();
        String encodeGson = gson.toJson(fileBlock);
        System.out.println("encode gson size: " + encodeGson.length() + ", " + encodeGson);

        FileBlock decodeJacksonFromGson = mapper.readValue(encodeGson, FileBlock.class);
        System.out.println(decodeJacksonFromGson);

        FileBlock decodeGsonFromJackson = gson.fromJson(encodeJackson, FileBlock.class);
        System.out.println(decodeGsonFromJackson);


    }

    @Data
    public static class FileBlock implements Serializable {
        Integer index;
        byte[] block;
    }

}

