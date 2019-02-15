package shibo.study.netty.spdy;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * @author zhangshibo
 */
public class SpdyChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext context;

    public SpdyChannelInitializer(SslContext context) {
        this.context = context;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        SSLEngine engine = context.newEngine(ch.alloc());
        engine.setUseClientMode(false);

        ch.pipeline()
                .addLast(new SslHandler(engine))
                .addLast(new DefaultSpdyOrHttpChooser(1024 * 1024, 1024 * 1024));
    }
}
