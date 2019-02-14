package shibo.study.netty.websocket.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.stream.ChunkedStream;
import lombok.extern.slf4j.Slf4j;
import shibo.study.netty.websocket.BinaryFrameHandler;
import shibo.study.netty.websocket.ContinuationFrameHandler;
import shibo.study.netty.websocket.TextFrameHandler;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author zhangshibo
 */
@Slf4j
public class WebSocketClient {

    private final URI uri;

    private EventLoopGroup group = new NioEventLoopGroup();
    private ChannelFuture f;

    public WebSocketClient(String uri) throws URISyntaxException {
        this.uri = new URI(uri);
    }

    public void start() throws InterruptedException {
        Bootstrap b = new Bootstrap();
        HttpHeaders headers = new DefaultHttpHeaders();
        WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory
                .newHandshaker(uri, WebSocketVersion.V13, null, true, headers);
        WebSocketHandshakeHandler handshakeHandler = new WebSocketHandshakeHandler();
        handshakeHandler.setHandshaker(handshaker);

        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new HttpClientCodec())
                                .addLast(new HttpObjectAggregator(65536))
                                .addLast(handshakeHandler)
                                .addLast(new TextFrameHandler())
                                .addLast(new BinaryFrameHandler())
                                .addLast(new ContinuationFrameHandler());
                    }
                });

        String host = uri.getHost();
        int port = uri.getPort() == -1 ? 80 : uri.getPort();
        f = b.connect(host, port).sync();
        log.info("WebSocketClient connected to {}:{}{}", host, port, uri.getPath());
        handshaker.handshake(f.channel());
        handshakeHandler.getHandshakeFuture().sync();
        log.debug("Handshake success.");
    }

    public void sendTxt(String txt) {
        TextWebSocketFrame frame = new TextWebSocketFrame(txt);
        f.channel().writeAndFlush(frame).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("Text: [{}] send success.", txt);
            } else {
                log.error("Text send failed.", future.cause());
            }
        });
    }

    public void sendBinary(InputStream is) throws Exception {
        ChunkedStream cis = new ChunkedStream(is);
        ByteBufAllocator allocator = f.channel().alloc();
        ByteBuf buf;
        while ((buf = cis.readChunk(allocator)) != null) {
            BinaryWebSocketFrame frame = new BinaryWebSocketFrame(Unpooled.buffer().writeBytes(buf));
            f.channel().writeAndFlush(frame).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    log.info("Binary data send success.");
                } else {
                    log.error("Binary send failed.", future.cause());
                }
            });
        }
    }

    public void close() throws InterruptedException {
        group.shutdownGracefully().sync();
    }
}
