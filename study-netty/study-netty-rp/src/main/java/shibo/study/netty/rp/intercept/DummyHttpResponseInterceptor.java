package shibo.study.netty.rp.intercept;

import io.netty.handler.codec.http.FullHttpResponse;

/**
 * @author zhangshibo
 */
public class DummyHttpResponseInterceptor implements HttpResponseInterceptor {
    @Override
    public void intercept(FullHttpResponse response) {
        // do nothing
    }
}
