package shibo.study.rpc.netty;

import org.apache.commons.lang3.RandomUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import shibo.study.common.NetUtils;
import shibo.study.proxy.ProxyFactoryManager;
import shibo.study.rpc.netty.client.ClientConfig;
import shibo.study.rpc.netty.client.RpcClient;
import shibo.study.rpc.netty.server.RpcServer;
import shibo.study.rpc.netty.server.ServerConfig;
import shibo.study.rpc.netty.service.ExpService;
import shibo.study.rpc.netty.service.ExpServiceImpl;
import shibo.study.rpc.netty.service.FibService;
import shibo.study.rpc.netty.service.FibServiceImpl;
import shibo.study.serialization.protostuff.ProtostuffSerializer;

/**
 * @author zhangshibo
 */
public class RpcTest {

    private RpcServer server;

    private RpcClient client;

    private FibService fibServiceReferred;

    private ExpService expServiceReferred;

    private FibService fibServiceSource;

    private ExpService expServiceSource;

    @Before
    public void before() {
        fibServiceSource = new FibServiceImpl();
        expServiceSource = new ExpServiceImpl();

        int port = NetUtils.getRandomAvaliablePort();
        initServer(port);
        initClient(port);

        server.start();
        client.start();

        fibServiceReferred = client.refer(FibService.class);
        expServiceReferred = client.refer(ExpService.class);
    }

    @Test
    public void test() {
        int n = RandomUtils.nextInt(0, 20) - 10;
        try {
            long res = fibServiceReferred.fib(n);
            Assert.assertEquals(fibServiceSource.fib(n), res);
        } catch (IllegalArgumentException e) {
            try {
                fibServiceSource.fib(n);
            } catch (IllegalArgumentException se) {
                Assert.assertEquals(e.getMessage(), se.getMessage());
            }
        }


        int base = RandomUtils.nextInt(0, 20) - 10;
        int exp = RandomUtils.nextInt(0, 10) - 5;
        try {
            long res = expServiceReferred.exp(base, exp);
            Assert.assertEquals(expServiceSource.exp(base, exp), res);
        } catch (IllegalArgumentException e) {
            try {
                expServiceSource.exp(base, exp);
            } catch (IllegalArgumentException se) {
                Assert.assertEquals(e.getMessage(), se.getMessage());
            }
        }
    }

    @After
    public void after() {
        client.shutdown();
        server.shutdown();
    }

    private void initServer(int port) {
        ServerConfig config = ServerConfig.builder()
                .port(port)
                .serializer(new ProtostuffSerializer())
                .build();
        RpcServer server = new RpcServer(config);
        server.export(fibServiceSource);
        server.export(expServiceSource);
        this.server = server;
    }

    private void initClient(int port) {
        ClientConfig config = ClientConfig.builder()
                .serverHost("127.0.0.1")
                .serverPort(port)
                .proxyFactory(ProxyFactoryManager.defaultProxyFactory())
                .serializer(new ProtostuffSerializer())
                .timeoutMills(100000L)
                .build();
        this.client = new RpcClient(config);
    }
}
