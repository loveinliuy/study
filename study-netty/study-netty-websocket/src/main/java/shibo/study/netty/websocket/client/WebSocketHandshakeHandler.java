package shibo.study.netty.websocket.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketHandshakeException;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author zhangshibo
 */
@Slf4j
public class WebSocketHandshakeHandler extends SimpleChannelInboundHandler<FullHttpResponse> {

    private WebSocketClientHandshaker handshaker;
    private ChannelPromise handshakeFuture;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        this.handshakeFuture = ctx.newPromise();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {
        boolean isHandshakeComplete = handshaker.isHandshakeComplete();
        log.debug("Check if handshake complete: {}", isHandshakeComplete);
        if (!isHandshakeComplete) {
            try {
                handshaker.finishHandshake(ctx.channel(), msg);
                handshakeFuture.setSuccess();
            } catch (WebSocketHandshakeException e) {
                String tip = String.format("WebSocket client failed to connect. status: %s, reason: %s", msg.status(),
                        msg.content().toString(StandardCharsets.UTF_8));
                log.error(tip, e);
                handshakeFuture.setFailure(new IllegalStateException(tip, e));
            }
        } else {
            log.warn("Handshake complete but still got FullHttpResponse (status={}, content={})", msg.status(),
                    msg.content().toString(StandardCharsets.UTF_8));
        }
    }

    public WebSocketClientHandshaker getHandshaker() {
        return handshaker;
    }

    public void setHandshaker(WebSocketClientHandshaker handshaker) {
        this.handshaker = handshaker;
    }

    public ChannelPromise getHandshakeFuture() {
        return handshakeFuture;
    }
}
