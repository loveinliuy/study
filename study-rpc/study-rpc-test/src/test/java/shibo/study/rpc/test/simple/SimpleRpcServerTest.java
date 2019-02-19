package shibo.study.rpc.test.simple;

import shibo.study.rpc.simple.RpcServer;
import shibo.study.rpc.test.service.FibServiceImpl;

/**
 * @author zhangshibo
 */
public class SimpleRpcServerTest {

    public static void main(String[] args) {
        RpcServer server = new RpcServer(1234);
        server.export(new FibServiceImpl());

        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
    }

}
