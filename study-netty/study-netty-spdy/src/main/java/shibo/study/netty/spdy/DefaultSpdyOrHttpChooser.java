package shibo.study.netty.spdy;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.spdy.SpdyFrameCodec;
import io.netty.handler.codec.spdy.SpdyHttpDecoder;
import io.netty.handler.codec.spdy.SpdyHttpEncoder;
import io.netty.handler.codec.spdy.SpdyHttpResponseStreamIdHandler;
import io.netty.handler.codec.spdy.SpdySessionHandler;
import io.netty.handler.codec.spdy.SpdyVersion;
import io.netty.handler.ssl.ApplicationProtocolNames;
import io.netty.handler.ssl.ApplicationProtocolNegotiationHandler;

/**
 * @author zhangshibo
 */
public class DefaultSpdyOrHttpChooser extends ApplicationProtocolNegotiationHandler {

    private final int maxSpdyContentLength;
    private final int maxHttpContentLength;

    public DefaultSpdyOrHttpChooser(int maxSpdyContentLength, int maxHttpContentLength) {
        super(ApplicationProtocolNames.HTTP_1_1);
        this.maxSpdyContentLength = maxSpdyContentLength;
        this.maxHttpContentLength = maxHttpContentLength;
    }

    @Override
    protected void configurePipeline(ChannelHandlerContext ctx, String protocol) throws Exception {
        if (ApplicationProtocolNames.SPDY_3_1.equals(protocol)) {
            configureSpdy(ctx, SpdyVersion.SPDY_3_1);
        } else if (ApplicationProtocolNames.HTTP_1_1.equals(protocol)) {
            configureHttp(ctx);
        } else {
            throw new IllegalArgumentException("Protocol not support: " + protocol);
        }
    }

    private void configureHttp(ChannelHandlerContext ctx) {
        ctx.pipeline()
                .addLast(new HttpServerCodec())
                .addLast(new HttpObjectAggregator(maxHttpContentLength))
                .addLast(new HttpRequestHandler());
    }

    private void configureSpdy(ChannelHandlerContext ctx, SpdyVersion spdyVersion) {
        ctx.pipeline()
                .addLast(new SpdyFrameCodec(spdyVersion))
                .addLast(new SpdySessionHandler(spdyVersion, true))
                .addLast(new SpdyHttpEncoder(spdyVersion))
                .addLast(new SpdyHttpDecoder(spdyVersion, maxSpdyContentLength))
                .addLast(new SpdyHttpResponseStreamIdHandler())
                .addLast(new SpdyRequestHandler());
    }
}
