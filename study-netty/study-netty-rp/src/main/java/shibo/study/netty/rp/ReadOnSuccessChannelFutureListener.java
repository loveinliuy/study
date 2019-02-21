package shibo.study.netty.rp;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
 * read the given channel {@link Channel#read()} while {@link ChannelFuture#isSuccess()}
 * or close the channel which ChannelFuture instance hold.
 *
 * @author zhangshibo
 */
public class ReadOnSuccessChannelFutureListener implements ChannelFutureListener {

    private final Channel ch;

    public ReadOnSuccessChannelFutureListener(Channel ch) {
        this.ch = ch;
    }

    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        if (future.isSuccess()) {
            ch.read();
        } else {
            future.channel().close();
        }
    }
}
