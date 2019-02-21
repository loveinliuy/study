package shibo.study.netty.rp;

import shibo.study.netty.rp.config.ProxyConfig;
import shibo.study.netty.rp.config.ServerConfig;

/**
 * @author zhangshibo
 */
public class TransparentRpServerTest {

    public static void main(String[] args) {
        ProxyConfig proxyConfig = ProxyConfig.builder()
                .host("172.18.11.150")
                .port(8080)
                .build();
        ServerConfig config = ServerConfig.builder()
                .parentGroupThreads(1)
                .childGroupThreads(0)
                .port(1234)
                .proxy(proxyConfig)
                .build();
        TransparentRpServer server = new TransparentRpServer(config);
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
    }
}
