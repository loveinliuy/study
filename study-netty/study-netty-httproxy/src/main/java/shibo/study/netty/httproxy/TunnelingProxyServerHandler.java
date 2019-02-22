package shibo.study.netty.httproxy;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpVersion;
import lombok.extern.slf4j.Slf4j;

/**
 * Tunneling TCP based, usually for https connections.
 *
 * @author zhangshibo
 */
@Slf4j
public class TunnelingProxyServerHandler extends ChannelInboundHandlerAdapter {

    private static final HttpResponseStatus CONNECTION_ESTABLISHED =
            new HttpResponseStatus(HttpResponseStatus.OK.code(), "Connection established");

    private final EventLoopGroup workerEventLoopGroup;
    private ChannelFuture proxyChannelFuture;

    public TunnelingProxyServerHandler(EventLoopGroup workerEventLoopGroup) {
        this.workerEventLoopGroup = workerEventLoopGroup;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (isConnectRequest(msg)) {
            HttpRequest request = (HttpRequest) msg;
            String[] hostAndPort = request.uri().split(":");
            String host = hostAndPort[0];
            int port = Integer.parseInt(hostAndPort[1]);
            establishConnection(ctx, host, port);
        } else {
            if (this.proxyChannelFuture == null) {
                super.channelRead(ctx, msg);
            } else {
                this.proxyChannelFuture.channel().writeAndFlush(msg);
            }
        }
    }

    private boolean isConnectRequest(Object msg) {
        return msg instanceof HttpRequest && HttpMethod.CONNECT.equals(((HttpRequest) msg).method());
    }

    private void establishConnection(ChannelHandlerContext ctx, String host, int port) {
        Bootstrap b = new Bootstrap();
        b.group(workerEventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(createClientChannelHandler(ctx));
        // block to create connection with real server.
        this.proxyChannelFuture = b.connect(host, port).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.debug("connected to remote server.");
            } else {
                log.error("could not connect to remote server", future.cause());
            }
            sendConnectionEstablishResult(ctx, future.isSuccess());
        });
    }

    private void sendConnectionEstablishResult(ChannelHandlerContext ctx, boolean success) {
        HttpResponseStatus status = success ? CONNECTION_ESTABLISHED : HttpResponseStatus.BAD_REQUEST;
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status);
        ctx.writeAndFlush(response).addListener((ChannelFutureListener) f -> {
            if (f.isSuccess()) {
                ctx.pipeline().remove(HttpServerCodec.class);
            } else {
                ctx.close();
            }
        });
    }

    /**
     * create a channel handler for handling the connection with real server.
     *
     * @return a channel handler
     */
    protected ChannelHandler createClientChannelHandler(ChannelHandlerContext ctx) {
        return new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline()
                        .addLast(new ProxyClientHandler(ctx.channel()));
            }
        };
    }
}
