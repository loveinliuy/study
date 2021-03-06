package shibo.study.netty.rp.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import shibo.study.netty.rp.ReadOnSuccessChannelFutureListener;
import shibo.study.netty.rp.util.NettyUtils;

/**
 * @author zhangshibo
 */
@Slf4j
public class TransparentBackendHandler extends ChannelInboundHandlerAdapter {

    private final Channel inboundChannel;

    public TransparentBackendHandler(Channel inboundChannel) {
        this.inboundChannel = inboundChannel;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.read();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        inboundChannel.writeAndFlush(msg)
                .addListener(new ReadOnSuccessChannelFutureListener(ctx.channel()));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NettyUtils.closeOnFlush(inboundChannel);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage(), cause);
        NettyUtils.closeOnFlush(ctx.channel());
    }
}
