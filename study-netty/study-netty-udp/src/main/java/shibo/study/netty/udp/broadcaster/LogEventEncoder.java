package shibo.study.netty.udp.broadcaster;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import shibo.study.netty.udp.LogEvent;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author zhangshibo
 */
public class LogEventEncoder extends MessageToMessageEncoder<LogEvent> {

    private final InetSocketAddress remoteAddress;

    public LogEventEncoder(InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, LogEvent logEvent, List<Object> out) throws Exception {
        byte[] file = logEvent.getLogfile().getBytes(StandardCharsets.UTF_8);
        byte[] msg = logEvent.getMsg().getBytes(StandardCharsets.UTF_8);
        int len = file.length + msg.length + 2;
        ByteBuf buf = ctx.alloc().buffer(len);
        buf.writeBytes(file);
        buf.writeByte(LogEvent.SEPARATOR);
        buf.writeBytes(msg);
        buf.writeByte(LogEvent.EOF);
        out.add(new DatagramPacket(buf, remoteAddress));
    }
}
