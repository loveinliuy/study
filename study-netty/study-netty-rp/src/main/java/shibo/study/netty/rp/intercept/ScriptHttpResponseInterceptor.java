package shibo.study.netty.rp.intercept;

import io.netty.handler.codec.http.FullHttpResponse;

import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * @author zhangshibo
 */
public class ScriptHttpResponseInterceptor extends AbstractHttpResponseInterceptor {

    private static final String SCRIPT_URI_START = "<script type=\"text/javascript\" src=\"";

    private static final String SCRIPT_URI_END = "\"></script>";

    private static final String SCRIPT_CONTENT_START = "<script type=\"text/javascript\" charset=\"utf-8\">";

    private static final String SCRIPT_CONTENT_END = "</script>";

    private final byte[] scriptAsByte;

    public ScriptHttpResponseInterceptor(URI uri) {
        String script = SCRIPT_URI_START + uri.toString() + SCRIPT_URI_END;
        this.scriptAsByte = script.getBytes(StandardCharsets.UTF_8);
    }

    public ScriptHttpResponseInterceptor(String content) {
        String script = SCRIPT_CONTENT_START + content + SCRIPT_CONTENT_END;
        this.scriptAsByte = script.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    protected void doIntercept(FullHttpResponse response) {
        response.content().writeBytes(scriptAsByte);
    }
}
