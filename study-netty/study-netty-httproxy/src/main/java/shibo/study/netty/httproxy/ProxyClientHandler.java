package shibo.study.netty.httproxy;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * handle the connections with real server.
 * read message and write to specific channel.
 *
 * @author zhangshibo
 */
@Slf4j
public class ProxyClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * channel to write
     */
    private final Channel outboundChannel;

    public ProxyClientHandler(Channel outboundChannel) {
        this.outboundChannel = outboundChannel;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        outboundChannel.writeAndFlush(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage(), cause);
        ctx.close();
    }
}
