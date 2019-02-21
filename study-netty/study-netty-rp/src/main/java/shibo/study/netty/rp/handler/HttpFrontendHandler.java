package shibo.study.netty.rp.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.logging.LoggingHandler;
import shibo.study.netty.rp.config.ProxyConfig;

/**
 * @author zhangshibo
 */
public class HttpFrontendHandler extends TransparentFrontendHandler {

    public HttpFrontendHandler(ProxyConfig config) {
        super(config);
    }

    @Override
    protected ChannelHandler createBackendChannelHandler(ChannelHandlerContext ctx) {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline()
                        .addLast(new LoggingHandler())
                        .addLast(new HttpClientCodec())
                        // allow max html page size: 1MB
                        .addLast(new HtmlOnlyResponseMessageAggregator(1024 * 1024, ctx.channel()))
                        .addLast(new HttpBackendHandler(ctx.channel(), config.getResponseInterceptor()));
            }
        };
    }
}
