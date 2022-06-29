package com.envisioniot.payload;

import com.envisioniot.payload.cbor.CborUploadMeasurepoint;
import com.envisioniot.payload.json.JsonUploadMeasurepoint;
import com.envisioniot.payload.proto.ProtoUploadMeasurepoint;
import com.envisioniot.payload.proto.ProtoUploadMeasurepointFixed;
import org.apache.commons.codec.binary.Hex;

/**
 * Desc:
 *
 * @author zhonghua.wu
 * @date 2022-06-29 13:56:45
 */
public class CodecTest {
    public static void main(String[] args) {
        testJson();
        testCbor();
        testProtobufFix();
    }

    public static void testJson() {
        JsonUploadMeasurepoint jsonUploadMeasurepoint = PayloadGenerator.json();
        JsonCodec jsonCodec = new JsonCodec();
        byte[] jsonBytes = jsonCodec.encode(jsonUploadMeasurepoint);
        printHex("json", jsonBytes);
    }

    public static void testCbor() {
        CborUploadMeasurepoint cborUploadMeasurepoint = PayloadGenerator.cbor();
        CborCodec cborCodec = new CborCodec();
        byte[] cborBytes = cborCodec.encode(cborUploadMeasurepoint);
        printHex("cbor", cborBytes);
    }

    public static void testProtobuf() {
        ProtoUploadMeasurepoint.UploadMeasurepoint protobufUploadMeasurepoint = PayloadGenerator.protobuf();
        ProtobufCodec protobufCodec = new ProtobufCodec();
        byte[] protobufBytes = protobufCodec.encode(protobufUploadMeasurepoint);
        printHex("protobuf", protobufBytes);

        ProtoUploadMeasurepoint.UploadMeasurepoint newProtobuf = protobufCodec.decode(protobufBytes);
        System.out.println("new protobuf: " + newProtobuf);
    }

    public static void testProtobufFix() {
        ProtoUploadMeasurepointFixed.UploadMeasurepoint protobufFixedUploadMeasurepoint = PayloadGenerator.protobufFixed();
        ProtobufFixedCodec protobufFixedCodec = new ProtobufFixedCodec();
        byte[] protobufFixBytes = protobufFixedCodec.encode(protobufFixedUploadMeasurepoint);
        printHex("protobufFix", protobufFixBytes);

        ProtoUploadMeasurepointFixed.UploadMeasurepoint newOne = protobufFixedCodec.decode(protobufFixBytes);
        System.out.println("new protobuf fixed: " + newOne);
    }


    public static void printHex(String codec, byte[] bytes) {
        System.out.println(codec + " encoded size: " + bytes.length);
        char[] hexChars = Hex.encodeHex(bytes);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hexChars.length; i++) {
            if (i % 2 == 0) {
                sb.append(" ");
            }
            sb.append(hexChars[i]);
        }
        System.out.println(codec + " hex: " + sb);
        System.out.println();
    }

}
