package shibo.study.netty.rp.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.AsciiString;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * only aggregates response html from backend server
 *
 * @author zhangshibo
 */
@Slf4j
public class HtmlOnlyResponseMessageAggregator extends HttpObjectAggregator {

    private static final AsciiString TEXT_HTML = AsciiString.cached("text/html");

    private static final ByteBuf BACKEND_RESPONSE_TOO_LARGE_CONTENT =
            Unpooled.copiedBuffer("backend server response too large.", StandardCharsets.UTF_8);
    private static final DefaultFullHttpResponse BACKEND_RESPONSE_TOO_LARGE =
            new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR, BACKEND_RESPONSE_TOO_LARGE_CONTENT);

    static {
        BACKEND_RESPONSE_TOO_LARGE.headers().set(HttpHeaderNames.CONTENT_LENGTH, BACKEND_RESPONSE_TOO_LARGE_CONTENT.readableBytes());
    }

    private boolean isIncomingResponseHtml = false;
    private Channel outboundChannel;

    public HtmlOnlyResponseMessageAggregator(int maxContentLength, Channel outboundChannel) {
        super(maxContentLength);
        this.outboundChannel = outboundChannel;
    }

    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        if (!super.acceptInboundMessage(msg)) {
            return false;
        }

        HttpObject obj = (HttpObject) msg;
        if (isStartMessage(obj)) {
            isIncomingResponseHtml = isHtmlResponse(obj);
        }
        return isIncomingResponseHtml;
    }

    @Override
    protected void handleOversizedMessage(ChannelHandlerContext ctx, HttpMessage oversized) throws Exception {
        if (oversized instanceof HttpRequest) {
            throw new IllegalStateException("backend server should never send a http request.");
        } else if (oversized instanceof HttpResponse) {
            // means that the backend server response a too large entity
            log.warn("backend server: {} return a too large response with size: {} expect: {}",
                    ctx.channel().remoteAddress(), HttpUtil.getContentLength(oversized, -1), maxContentLength());
            outboundChannel.writeAndFlush(BACKEND_RESPONSE_TOO_LARGE.retainedDuplicate())
                    .addListener((ChannelFutureListener) future -> {
                        if (!future.isSuccess()) {
                            log.debug("could not send backend too large response to client.", future.cause());
                            outboundChannel.close();
                            ctx.close();
                        }
                    });
        } else {
            throw new IllegalStateException("http message must be http request or a http response");
        }
    }

    private static boolean isHtmlResponse(HttpObject msg) {
        if (msg instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) msg;
            CharSequence mime = HttpUtil.getMimeType(response);
            return TEXT_HTML.contentEquals(mime);
        } else {
            return false;
        }
    }

}
