package com.envisioniot.payload;

import com.envisioniot.payload.cbor.CborUploadMeasurepoint;
import com.envisioniot.payload.json.JsonUploadMeasurepoint;
import com.envisioniot.payload.proto.ProtoUploadMeasurepointProto;
import org.apache.commons.codec.binary.Hex;

/**
 * Desc:
 *
 * @author zhonghua.wu
 * @date 2022-06-29 13:56:45
 */
public class CodecTest {
    public static void main(String[] args) {
        JsonUploadMeasurepoint jsonUploadMeasurepoint = PayloadGenerator.json();
        JsonCodec jsonCodec = new JsonCodec();
        byte[] jsonBytes = jsonCodec.encode(jsonUploadMeasurepoint);
        printHex("json", jsonBytes);

        CborUploadMeasurepoint cborUploadMeasurepoint = PayloadGenerator.cbor();
        CborCodec cborCodec = new CborCodec();
        byte[] cborBytes = cborCodec.encode(cborUploadMeasurepoint);
        printHex("cbor", cborBytes);

        ProtoUploadMeasurepointProto.UploadMeasurepoint protobufUploadMeasurepoint = PayloadGenerator.protobuf();
        ProtobufCodec protobufCodec = new ProtobufCodec();
        byte[] protobufBytes = protobufCodec.encode(protobufUploadMeasurepoint);
        printHex("protobuf", protobufBytes);

        ProtoUploadMeasurepointProto.UploadMeasurepoint newProtobuf = protobufCodec.decode(protobufBytes);
        System.out.println("new protobuf: " + newProtobuf);

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
