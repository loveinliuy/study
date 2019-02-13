package shibo.study.netty.echo;

import lombok.extern.slf4j.Slf4j;
import shibo.study.netty.echo.client.EchoClient;

/**
 * @author zhangshibo
 */
@Slf4j
public class EchoClientTest {

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 2) {
            log.error("Usage: EchoClient <host> <port>");
            return;
        }
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        EchoClient client = new EchoClient(host, port);
        client.start();
    }
}
