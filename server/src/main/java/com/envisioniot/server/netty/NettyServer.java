package com.envisioniot.server.netty;

import com.envisioniot.payload.proto.ProtoUploadMeasurepointFixed;
import com.envisioniot.transport.ServerManager;
import com.envisioniot.transport.pipeline.ProtoBufReadPipelineInitializer;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.json.JsonObjectDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.*;

/**
 * Desc:
 *
 * @author zhonghua.wu
 * @date 2022-06-26 22:55:19
 */
@Slf4j
@Service
public class NettyServer {

    private final static int JSON_PORT = 8880;
    private final static int CBOR_PORT = 8881;
    private final static int PROTOBUF_PORT = 8882;

    private final ServerManager jsonServerManager;
    private final ServerManager cborServerManager;
    private final ServerManager protobufServerManager;

    private final ExecutorService executorService = new ThreadPoolExecutor(3, 3,
            1, TimeUnit.SECONDS,
            new SynchronousQueue<>(),
            new ThreadFactoryBuilder().setNameFormat("server-starter-%d").build());

    public NettyServer() {
        jsonServerManager = new ServerManager(JSON_PORT, pipeline -> {
            pipeline.addLast(new JsonObjectDecoder());
            pipeline.addLast(new JsonDecoderMessageHandler());
        });

        cborServerManager = new ServerManager(CBOR_PORT, pipeline -> {
            pipeline.addLast(new ByteArrayDecoder());
            pipeline.addLast(new CborDecoderMessageHandler());
        });

        protobufServerManager = new ServerManager(PROTOBUF_PORT, new ProtoBufReadPipelineInitializer<>(
                ProtoUploadMeasurepointFixed.UploadMeasurepoint.getDefaultInstance(),
                ProtobufDecoderMessageHandler::new));
    }

    @PostConstruct
    public void init() {
        CompletableFuture.runAsync(jsonServerManager::start, executorService);
        log.info("start server for json with port[{}]", JSON_PORT);
        CompletableFuture.runAsync(cborServerManager::start, executorService);
        log.info("start server for cbor with port[{}]", CBOR_PORT);
        CompletableFuture.runAsync(protobufServerManager::start, executorService);
        log.info("start server for protobuf with port[{}]", PROTOBUF_PORT);
    }

    @PreDestroy
    public void destroy() {
        CompletableFuture.runAsync(jsonServerManager::shutdown, executorService);
        CompletableFuture.runAsync(cborServerManager::shutdown, executorService);
        CompletableFuture.runAsync(protobufServerManager::shutdown, executorService);
    }


}
