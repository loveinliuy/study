package shibo.study.netty.udp;

import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.socket.DatagramPacket;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import shibo.study.common.NetUtils;
import shibo.study.netty.udp.broadcaster.LogEventEncoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author zhangshibo
 */
public class LogEventEncoderTest {

    @Test
    public void testLogEventEncoder() throws IOException {
        String log = RandomStringUtils.randomAlphabetic(1000);
        String msg = RandomStringUtils.randomAlphabetic(500000);
        LogEvent payload = new LogEvent(log, msg);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(log.getBytes(StandardCharsets.UTF_8));
        bos.write(LogEvent.SEPARATOR);
        bos.write(msg.getBytes(StandardCharsets.UTF_8));
        bos.write(LogEvent.EOF);
        final byte[] expect = bos.toByteArray();

        EmbeddedChannel channel = new EmbeddedChannel(
                new LogEventEncoder(new InetSocketAddress(NetUtils.getRandomAvaliablePort())));

        assertTrue(channel.writeOutbound(payload));
        assertTrue(channel.finish());

        byte[] res = new byte[expect.length];
        DatagramPacket packet = channel.readOutbound();
        packet.content().getBytes(0, res);
        assertArrayEquals(bos.toByteArray(), res);
    }

}