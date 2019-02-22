package shibo.study.netty.httproxy;

/**
 * @author zhangshibo
 */
public class HttpProxyServerTest {

    public static void main(String[] args) {
        ProxyConfig config = ProxyConfig.builder()
                .port(1234)
                .parentGroupThreads(1)
                .childGroupThreads(1)
                .proxyGroupThreads(1)
                .build();
        HttpProxyServer server = new HttpProxyServer(config);
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
    }
}
