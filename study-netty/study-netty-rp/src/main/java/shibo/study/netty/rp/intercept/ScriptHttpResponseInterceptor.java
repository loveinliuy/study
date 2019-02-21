package shibo.study.netty.rp.intercept;

import io.netty.handler.codec.http.FullHttpResponse;

import java.nio.charset.StandardCharsets;

/**
 * @author zhangshibo
 */
public class ScriptHttpResponseInterceptor extends AbstractHttpResponseInterceptor {

    private static final String SCRIPT_START = "<script src=\"";

    private static final String SCRIPT_END = "\"></script>";

    private final byte[] scriptAsByte;

    public ScriptHttpResponseInterceptor(String src) {
        String script = SCRIPT_START + src + SCRIPT_END;
        this.scriptAsByte = script.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    protected void doIntercept(FullHttpResponse response) {
        response.content().writeBytes(scriptAsByte);
    }
}
