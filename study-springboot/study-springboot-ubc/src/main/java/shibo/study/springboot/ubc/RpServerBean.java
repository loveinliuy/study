package shibo.study.springboot.ubc;

import shibo.study.netty.rp.HttpRpServer;
import shibo.study.netty.rp.config.ProxyConfig;
import shibo.study.netty.rp.config.ServerConfig;
import shibo.study.netty.rp.intercept.ScriptHttpResponseInterceptor;

import java.net.URI;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangshibo
 */
public class RpServerBean {

    private final ExecutorService runner = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(1), r -> new Thread(r, "rp-server-runner"));

    private HttpRpServer server;

    public RpServerBean(URI loaderURI, RpServerProperties prop) {
        ProxyConfig proxyConfig = ProxyConfig.builder()
                .host(prop.getProxyHost())
                .port(prop.getProxyPort())
                .responseInterceptor(new ScriptHttpResponseInterceptor(loaderURI))
                .build();
        ServerConfig config = ServerConfig.builder()
                .port(prop.getPort())
                .parentGroupThreads(prop.getParentGroupThreads())
                .childGroupThreads(prop.getWorkerGroupThreads())
                .proxy(proxyConfig)
                .build();
        this.server = new HttpRpServer(config);
    }

    public void init() {
        runner.submit(() -> server.start());
    }

    public void destroy() {
        server.shutdown();
    }
}
