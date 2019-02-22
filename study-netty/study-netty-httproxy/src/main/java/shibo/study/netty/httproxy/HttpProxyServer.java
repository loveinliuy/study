package shibo.study.netty.httproxy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangshibo
 */
@Slf4j
public class HttpProxyServer {

    private final ProxyConfig config;
    private EventLoopGroup parentGroup;
    private EventLoopGroup childGroup;
    private EventLoopGroup proxyGroup;
    private Channel channel;

    public HttpProxyServer(ProxyConfig config) {
        this.config = config;
    }

    private ChannelHandler createChildHandler(ProxyConfig config) {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline()
                        .addLast(new HttpServerCodec())
                        .addLast(new TunnelingProxyServerHandler(proxyGroup));
            }
        };
    }

    public void start() {
        parentGroup = new NioEventLoopGroup(config.getParentGroupThreads());
        childGroup = new NioEventLoopGroup(config.getChildGroupThreads());
        proxyGroup = new NioEventLoopGroup(config.getProxyGroupThreads());
        ServerBootstrap b = new ServerBootstrap();
        b.group(parentGroup, childGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler())
                .childHandler(createChildHandler(config));
        ChannelFuture f = b.bind(config.getPort()).syncUninterruptibly();
        log.info("HttpProxyServer start and listen on port: {}", config.getPort());
        channel = f.channel();
    }

    public void shutdown() {
        if (channel != null) {
            channel.close();
        }
        childGroup.shutdownGracefully();
        parentGroup.shutdownGracefully();
    }


}
