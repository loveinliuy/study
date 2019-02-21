package shibo.study.netty.rp.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import shibo.study.netty.rp.ReadOnSuccessChannelFutureListener;
import shibo.study.netty.rp.config.ProxyConfig;
import shibo.study.netty.rp.util.NettyUtils;

/**
 * @author zhangshibo
 */
@Slf4j
public class TransparentFrontendHandler extends ChannelInboundHandlerAdapter {

    protected final ProxyConfig config;

    private Channel outboundChannel;

    public TransparentFrontendHandler(ProxyConfig config) {
        this.config = config;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel inboundChannel = ctx.channel();

        Bootstrap b = new Bootstrap();
        b.group(inboundChannel.eventLoop())
                .channel(ctx.channel().getClass())
                .handler(createBackendChannelHandler(ctx))
                .option(ChannelOption.AUTO_READ, false);

        ChannelFuture f = b.connect(config.getHost(), config.getPort());
        outboundChannel = f.channel();

        f.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                inboundChannel.read();
            } else {
                log.debug("Can not connect to backend server, close the connection with client");
                inboundChannel.close();
            }
        });
    }

    protected ChannelHandler createBackendChannelHandler(ChannelHandlerContext ctx) {
        return new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline()
                        .addLast(new LoggingHandler())
                        .addLast(new HttpClientCodec())
                        .addLast(new HttpObjectAggregator(1024 * 1024))
                        .addLast(new TransparentBackendHandler(ctx.channel()));
            }
        };
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (outboundChannel.isActive()) {
            outboundChannel.writeAndFlush(msg)
                    .addListener(new ReadOnSuccessChannelFutureListener(ctx.channel()));
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (outboundChannel != null) {
            NettyUtils.closeOnFlush(outboundChannel);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage(), cause);
        NettyUtils.closeOnFlush(ctx.channel());
    }

}
