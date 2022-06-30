package com.envisioniot.client;

import com.envisioniot.payload.PayloadGenerator;
import com.envisioniot.payload.json.JsonUploadMeasurepoint;
import com.envisioniot.payload.proto.ProtoUploadMeasurepointFixed;
import com.envisioniot.transport.ClientManager;
import com.envisioniot.transport.Endpoint;
import com.envisioniot.transport.pipeline.PipelineInitializer;
import com.envisioniot.transport.pipeline.ProtoBufWritePipelineInitializer;
import io.netty.channel.ChannelPipeline;
import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

/**
 * Desc:
 *
 * @author zhonghua.wu
 * @date 2022-06-26 23:14:34
 */
public class NettyClient {

    private final static String HOST = "localhost";

    private final static int JSON_PORT = 8880;
    private final static int CBOR_PORT = 8881;
    private final static int PROTOBUF_PORT = 8882;

    public static void main(String[] args) {
        //protobufFixClient();
        jsonClient();
    }

    @SneakyThrows
    public static void protobufFixClient() {
        ClientManager clientManager = new ClientManager(1, new ProtoBufWritePipelineInitializer());

        ProtoUploadMeasurepointFixed.UploadMeasurepoint uploadMeasurepoint = PayloadGenerator.protobufFixed();
        clientManager.sendMessage(new Endpoint(HOST, PROTOBUF_PORT), uploadMeasurepoint).await();
        clientManager.shutdown();
    }

    @SneakyThrows
    public static void jsonClient() {
        ClientManager clientManager = new ClientManager(1, new PipelineInitializer() {
            @Override
            public void init(ChannelPipeline pipeline) throws Exception {
                pipeline.addLast(new JsonEncoderMessageHandler());
            }
        });
        JsonUploadMeasurepoint jsonUploadMeasurepoint = PayloadGenerator.json();
        clientManager.sendMessage(new Endpoint(HOST, JSON_PORT), jsonUploadMeasurepoint).await();
        clientManager.shutdown();
    }

}
