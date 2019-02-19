package shibo.study.rpc.test;

import org.junit.Test;
import shibo.study.common.NetUtils;
import shibo.study.rpc.api.Exporter;
import shibo.study.rpc.api.Referer;
import shibo.study.rpc.simple.RpcClient;
import shibo.study.rpc.simple.RpcServer;
import shibo.study.rpc.test.service.ExpService;
import shibo.study.rpc.test.service.FibService;

/**
 * @author zhangshibo
 */
public class SimpleRpcTest extends AbstractRpcTest {

    private int port = NetUtils.getRandomAvaliablePort();

    private RpcServer server;
    private RpcClient client;

    @Override
    public void before() {
        initServer();
        // make sure server has been init
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException ignore) {
        }
        initClient();
    }

    @Test
    public void testExp() {
        server.export(super.expServiceSource);
        super.expServiceReferred = client.refer(ExpService.class);

        super.testExp(false);
    }

    @Test
    public void testFib() {
        server.export(super.fibServiceSource);
        super.fibServiceReferred = client.refer(FibService.class);

        super.testFib(false);
    }


    @Override
    protected Referer initClient() {
        client = new RpcClient("127.0.0.1", port);
        return client;
    }

    @Override
    protected Exporter initServer() {
        server = new RpcServer(port);
        return server;
    }

    @Override
    protected void shutdownClientAndServer() {
        server.shutdown();
    }
}
