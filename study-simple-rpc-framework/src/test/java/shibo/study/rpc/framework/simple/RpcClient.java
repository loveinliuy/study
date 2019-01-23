package shibo.study.rpc.framework.simple;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author zhangshibo
 */
@Log4j2
public class RpcClient {

    public static void main(String[] args) throws InterruptedException {
        TestService service = RpcFramework.refer(TestService.class, "127.0.0.1", 1234);
        for (int i = 0; i < 20; i++) {
            String name = RandomStringUtils.randomAlphabetic(4);
            log.info("Calling server with name: {}", name);
            String result = service.hello(name);
            log.info("Server return result: {}", result);
            Thread.sleep(500L);
        }
    }
}
