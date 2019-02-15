package shibo.study.netty.udp.monitor;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import shibo.study.netty.udp.LogEvent;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author zhangshibo
 */
public class LogEventDecoder extends MessageToMessageDecoder<DatagramPacket> {
    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) throws Exception {
        ByteBuf data = msg.content();
        int i = data.indexOf(0, data.readableBytes(), LogEvent.SEPARATOR);
        int eof = data.indexOf(i, data.readableBytes(), LogEvent.EOF);
        String filename = data.slice(0, i).toString(StandardCharsets.UTF_8);
        String logMsg = data.slice(i + 1, eof - i - 1).toString(StandardCharsets.UTF_8);
        LogEvent event = new LogEvent(msg.recipient(), System.currentTimeMillis(), filename, logMsg);

        out.add(event);
    }
}
