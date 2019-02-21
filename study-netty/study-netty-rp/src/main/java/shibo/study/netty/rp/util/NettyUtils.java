package shibo.study.netty.rp.util;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

/**
 * @author zhangshibo
 */
public final class NettyUtils {

    private NettyUtils() {
    }

    /**
     * write an empty buffer and close channel if channel is active
     *
     * @param ch channel
     */
    public static void closeOnFlush(Channel ch) {
        if (ch.isActive()) {
            ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }
}
