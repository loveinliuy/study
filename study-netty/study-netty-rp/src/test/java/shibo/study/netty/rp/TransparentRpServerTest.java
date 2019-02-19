package shibo.study.netty.rp;

import shibo.study.netty.rp.config.ProxyConfig;
import shibo.study.netty.rp.config.ServerConfig;

/**
 * @author zhangshibo
 */
public class TransparentRpServerTest {

    public static void main(String[] args) {
        ServerConfig config = ServerConfig.builder()
                .parentGroupThreads(1)
                .childGroupThreads(5)
                .port(1234)
                .proxy(new ProxyConfig("172.18.11.150", 8080))
                .build();
        TransparentRpServer server = new TransparentRpServer(config);
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
    }
}
