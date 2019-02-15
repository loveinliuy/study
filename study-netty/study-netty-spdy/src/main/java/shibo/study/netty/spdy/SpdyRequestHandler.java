package shibo.study.netty.spdy;

import io.netty.channel.ChannelHandler;

/**
 * @author zhangshibo
 */
@ChannelHandler.Sharable
public class SpdyRequestHandler extends HttpRequestHandler {

    @Override
    protected String getContent() {
        return "This content is transmitted via SPDY\r\n";
    }
}
