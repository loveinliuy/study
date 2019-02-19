package shibo.study.rpc.test;

import org.junit.Test;
import shibo.study.common.NetUtils;
import shibo.study.proxy.ProxyFactoryManager;
import shibo.study.rpc.api.Exporter;
import shibo.study.rpc.api.Referer;
import shibo.study.rpc.netty.client.ClientConfig;
import shibo.study.rpc.netty.client.RpcClient;
import shibo.study.rpc.netty.server.RpcServer;
import shibo.study.rpc.netty.server.ServerConfig;
import shibo.study.serialization.protostuff.ProtostuffSerializer;

/**
 * @author zhangshibo
 */
public class NettyRpcTest extends AbstractRpcTest {

    private int port = NetUtils.getRandomAvaliablePort();

    private RpcServer server;

    private RpcClient client;

    @Test
    public void testRpc() {
        super.testExp(true);
        super.testFib(true);
    }

    @Override
    protected Referer initClient() {
        ClientConfig config = ClientConfig.builder()
                .serverHost("127.0.0.1")
                .serverPort(port)
                .proxyFactory(ProxyFactoryManager.defaultProxyFactory())
                .serializer(new ProtostuffSerializer())
                .timeoutMills(1000L)
                .build();
        this.client = new RpcClient(config);
        client.start();
        return client;
    }

    @Override
    protected Exporter initServer() {
        ServerConfig config = ServerConfig.builder()
                .port(port)
                .serializer(new ProtostuffSerializer())
                .build();
        this.server = new RpcServer(config);
        server.start();
        return server;
    }

    @Override
    protected void shutdownClientAndServer() {
        client.shutdown();
        server.shutdown();
    }
}
