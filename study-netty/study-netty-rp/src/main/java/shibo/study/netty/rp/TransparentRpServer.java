package shibo.study.netty.rp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import shibo.study.netty.rp.config.ServerConfig;
import shibo.study.netty.rp.handler.TransparentFrontendHandler;

/**
 * @author zhangshibo
 */
@Slf4j
public class TransparentRpServer {

    private final ServerConfig config;

    private final EventLoopGroup parentGroup;
    private final EventLoopGroup childGroup;

    private Channel channel;

    public TransparentRpServer(ServerConfig config) {
        this.config = config;
        this.parentGroup = new NioEventLoopGroup(config.getParentGroupThreads());
        this.childGroup = new NioEventLoopGroup(config.getChildGroupThreads());
    }

    public void start() {
        ServerBootstrap b = new ServerBootstrap();
        b.group(parentGroup, childGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler())
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new LoggingHandler())
                                .addLast(new TransparentFrontendHandler(config.getProxy()));
                    }
                })
                .childOption(ChannelOption.AUTO_READ, false);

        ChannelFuture f = b.bind(config.getPort()).syncUninterruptibly();
        log.info("TransparentRpServer start listening at port: {}", config.getPort());
        channel = f.channel();
    }

    public void shutdown() {
        if (channel != null) {
            channel.close().syncUninterruptibly();
        }
        childGroup.shutdownGracefully();
        parentGroup.shutdownGracefully();
    }
}
