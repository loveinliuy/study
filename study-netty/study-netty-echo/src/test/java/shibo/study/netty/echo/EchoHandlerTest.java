package shibo.study.netty.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import shibo.study.netty.echo.client.EchoClientHandler;
import shibo.study.netty.echo.server.EchoServerHandler;

import java.nio.charset.StandardCharsets;

/**
 * @author zhangshibo
 */
public class EchoHandlerTest {

    @Test
    public void testEchoServerHandler() {
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(RandomStringUtils.randomAlphabetic(10).getBytes(StandardCharsets.UTF_8));

        EmbeddedChannel channel = new EmbeddedChannel(new EchoServerHandler());
        channel.writeInbound(buf);

        Assert.assertTrue(channel.finish());
    }

    @Test
    public void testEchoClientHandler() {
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(RandomStringUtils.randomAlphabetic(10).getBytes(StandardCharsets.UTF_8));

        EmbeddedChannel channel = new EmbeddedChannel(new EchoClientHandler());
        channel.writeInbound(buf);

        Assert.assertTrue(channel.finish());

        ByteBuf out = channel.readOutbound();
        Assert.assertEquals("Netty rocks!", out.toString(StandardCharsets.UTF_8));

        Assert.assertNull(channel.readOutbound());
    }
}
