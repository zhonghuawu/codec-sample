package com.envisioniot.client;

import com.envisioniot.payload.json.JsonUploadMeasurepoint;
import com.envisioniot.transport.ClientManager;
import com.envisioniot.transport.Endpoint;
import com.envisioniot.transport.pipeline.ProtoBufWritePipelineInitializer;

/**
 * Desc:
 *
 * @author zhonghua.wu
 * @date 2022-06-26 23:14:34
 */
public class NettyClient {

    private final static String HOST = "localhost";

    private final static int JSON_PORT = 8888;
    private final static int CBOR_PORT = 8889;
    private final static int PROTOBUF_PORT = 8890;

    public static void main(String[] args) {
        ClientManager clientManager = new ClientManager(1, new ProtoBufWritePipelineInitializer());

        JsonUploadMeasurepoint jsonUploadMeasurepoint = new JsonUploadMeasurepoint();
        clientManager.sendMessage(new Endpoint(HOST, JSON_PORT), jsonUploadMeasurepoint);
    }

}
