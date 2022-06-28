package com.envisioniot.transport;

import com.google.protobuf.ByteString;

/**
 * Some efficient APIs are not exposed by protobuf. Here we pose it
 * for our internal usage.
 *
 * @author jian.zhang4
 */
public class ProtocolBufHelper {

    public static ByteString createByteString(byte[] data) {
        return ByteString.copyFrom(data);
    }

}
