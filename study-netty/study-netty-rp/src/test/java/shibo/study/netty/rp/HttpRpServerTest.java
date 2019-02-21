package shibo.study.netty.rp;

import shibo.study.netty.rp.config.ProxyConfig;
import shibo.study.netty.rp.config.ServerConfig;
import shibo.study.netty.rp.intercept.ScriptHttpResponseInterceptor;

/**
 * @author zhangshibo
 */
public class HttpRpServerTest {

    public static void main(String[] args) {
        String scriptSrc = "";
        ProxyConfig proxyConfig = ProxyConfig.builder()
                .host("172.18.11.150")
                .port(8080)
                .responseInterceptor(new ScriptHttpResponseInterceptor(scriptSrc))
                .build();
        ServerConfig config = ServerConfig.builder()
                .parentGroupThreads(1)
                .childGroupThreads(0)
                .port(1234)
                .proxy(proxyConfig)
                .build();
        HttpRpServer server = new HttpRpServer(config);
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
    }
}
