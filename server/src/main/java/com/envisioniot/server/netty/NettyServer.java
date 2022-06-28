package com.envisioniot.server.netty;

import com.envisioniot.payload.proto.ProtoUploadMeasurepointProto;
import com.envisioniot.transport.ServerManager;
import com.envisioniot.transport.pipeline.ProtoBufRWPipelineInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Desc:
 *
 * @author zhonghua.wu
 * @date 2022-06-26 22:55:19
 */
@Slf4j
@Service
public class NettyServer {

    private final static int JSON_PORT = 8888;
    private final static int CBOR_PORT = 8889;
    private final static int PROTOBUF_PORT = 8890;

    private ServerManager serverManager;

    public NettyServer() {
        serverManager = new ServerManager(JSON_PORT, new ProtoBufRWPipelineInitializer<>(
                ProtoUploadMeasurepointProto.UploadMeasurepoint.getDefaultInstance(),
                MessageHandler::new));
    }


}
