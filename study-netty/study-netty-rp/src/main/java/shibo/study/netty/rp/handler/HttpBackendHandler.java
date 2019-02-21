package shibo.study.netty.rp.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import shibo.study.netty.rp.intercept.HttpResponseInterceptor;

/**
 * @author zhangshibo
 */
public class HttpBackendHandler extends TransparentBackendHandler {

    private HttpResponseInterceptor responseInterceptor;

    public HttpBackendHandler(Channel inboundChannel, HttpResponseInterceptor responseInterceptor) {
        super(inboundChannel);
        this.responseInterceptor = responseInterceptor;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpResponse) {
            // http response aggregated.
            FullHttpResponse response = (FullHttpResponse) msg;
            responseInterceptor.intercept(response);
            super.channelRead(ctx, response);
        } else {
            super.channelRead(ctx, msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
