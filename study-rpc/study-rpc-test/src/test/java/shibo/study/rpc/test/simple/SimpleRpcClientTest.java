package shibo.study.rpc.test.simple;

import lombok.extern.log4j.Log4j2;
import shibo.study.rpc.simple.RpcClient;
import shibo.study.rpc.test.service.FibService;

/**
 * @author zhangshibo
 */
@Log4j2
public class SimpleRpcClientTest {

    public static void main(String[] args) {
        RpcClient client = new RpcClient("127.0.0.1", 1234);
        FibService service = client.refer(FibService.class);
        int n = 5;
        long res = service.fib(n);
        log.info("rpc result: fib({})={}", n, res);
    }
}
