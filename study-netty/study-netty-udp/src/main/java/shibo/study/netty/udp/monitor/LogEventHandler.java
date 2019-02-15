package shibo.study.netty.udp.monitor;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import shibo.study.netty.udp.LogEvent;

/**
 * @author zhangshibo
 */
@Slf4j
public class LogEventHandler extends SimpleChannelInboundHandler<LogEvent> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogEvent msg) throws Exception {
        log.info("{} [{}] [{}] [{}]", msg.getReceivedTimestamp(), msg.getSource(), msg.getLogfile(), msg.getMsg());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage(), cause);
        ctx.close();
    }
}
