package shibo.study.rpc.netty.server;

import lombok.Builder;
import lombok.Getter;
import shibo.study.serialization.Serializer;

/**
 * @author zhangshibo
 */
@Builder
@Getter
public class ServerConfig {

    private int port;

    private Serializer serializer;
}
