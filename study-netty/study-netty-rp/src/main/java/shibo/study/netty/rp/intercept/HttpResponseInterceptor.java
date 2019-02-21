package shibo.study.netty.rp.intercept;

import io.netty.handler.codec.http.FullHttpResponse;

/**
 * @author zhangshibo
 */
public interface HttpResponseInterceptor {

    /**
     * intercept the http response from backend server
     *
     * @param response
     */
    void intercept(FullHttpResponse response);
}
