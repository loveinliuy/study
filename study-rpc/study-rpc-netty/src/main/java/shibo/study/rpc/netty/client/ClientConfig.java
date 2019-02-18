package shibo.study.rpc.netty.client;

import lombok.Builder;
import lombok.Getter;
import shibo.study.proxy.ProxyFactory;
import shibo.study.serialization.Serializer;

/**
 * @author zhangshibo
 */
@Builder
@Getter
public class ClientConfig {

    private String serverHost;

    private int serverPort;

    private Serializer serializer;

    private long timeoutMills;

    private ProxyFactory proxyFactory;
}
