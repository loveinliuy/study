package shibo.study.netty.spdy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;

import java.net.InetSocketAddress;

/**
 * @author zhangshibo
 */
public class SpdyServer {

    private final NioEventLoopGroup group = new NioEventLoopGroup();

    private final SslContext context;
    private Channel channel;

    public SpdyServer(SslContext context) {
        this.context = context;
    }

    public ChannelFuture start(InetSocketAddress address) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(group)
                .channel(NioServerSocketChannel.class)
                .childHandler(new SpdyChannelInitializer(context));

        ChannelFuture future = bootstrap.bind(address);
        future.syncUninterruptibly();
        channel = future.channel();
        return future;
    }

    public void destroy() {
        if (channel != null) {
            channel.close();
        }
        group.shutdownGracefully();
    }
}
