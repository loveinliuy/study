package shibo.study.netty.rp.intercept;

import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.util.AsciiString;

/**
 * @author zhangshibo
 */
public abstract class AbstractHttpResponseInterceptor implements HttpResponseInterceptor {

    private static final AsciiString PROXIED_HEADER = AsciiString.cached("X-Proxied-By");
    private static final AsciiString PROXIED_VAL = AsciiString.cached("http-rp-server");

    @Override
    public void intercept(FullHttpResponse response) {
        response.headers().set(PROXIED_HEADER, PROXIED_VAL);
        doIntercept(response);
        // reset the content length
        if (response.headers().contains(HttpHeaderNames.CONTENT_LENGTH)) {
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        }
    }

    /**
     * intercepts the backend response
     *
     * @param response backend response
     */
    protected abstract void doIntercept(FullHttpResponse response);

}
