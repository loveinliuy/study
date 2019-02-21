package shibo.study.netty.rp.config;

import lombok.Builder;
import lombok.Getter;
import shibo.study.netty.rp.intercept.DummyHttpResponseInterceptor;
import shibo.study.netty.rp.intercept.HttpResponseInterceptor;

/**
 * @author zhangshibo
 */
@Builder
@Getter
public class ProxyConfig {

    private String host;

    private int port;

    private int maxHtmlPageSize = 1024 * 1024;

    /**
     * interceptor deal the backend response.
     */
    private HttpResponseInterceptor responseInterceptor = new DummyHttpResponseInterceptor();
}
