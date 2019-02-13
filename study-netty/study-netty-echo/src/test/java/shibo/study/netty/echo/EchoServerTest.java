package shibo.study.netty.echo;

import lombok.extern.slf4j.Slf4j;
import shibo.study.netty.echo.server.EchoServer;

/**
 * @author zhangshibo
 */
@Slf4j
public class EchoServerTest {

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 1) {
            log.error("Usage: EchoServer <port>");
            return;
        }
        int port = Integer.parseInt(args[0]);
        EchoServer server = new EchoServer(port);
        server.start();
    }
}
