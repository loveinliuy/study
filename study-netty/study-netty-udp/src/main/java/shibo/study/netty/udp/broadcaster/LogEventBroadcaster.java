package shibo.study.netty.udp.broadcaster;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.extern.slf4j.Slf4j;
import shibo.study.netty.udp.LogEvent;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;

/**
 * @author zhangshibo
 */
@Slf4j
public class LogEventBroadcaster {

    private final Bootstrap bootstrap;
    private final File file;
    private final EventLoopGroup group;

    public LogEventBroadcaster(InetSocketAddress address, File file) {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new LogEventEncoder(address));
        this.file = file;
    }

    public void run() throws IOException, InterruptedException {
        Channel ch = bootstrap.bind(0).sync().channel();
        log.info("LogEventBroadcaster running");

        long pointer = 0;
        while (true) {
            long len = file.length();
            if (len < pointer) {
                pointer = len;
            } else if (len > pointer) {
                RandomAccessFile raf = new RandomAccessFile(file, "r");
                raf.seek(pointer);
                String line;
                while ((line = raf.readLine()) != null) {
                    ch.writeAndFlush(new LogEvent(file.getAbsolutePath(), line));
                }
                pointer = raf.getFilePointer();
                raf.close();
            } else {
                try {
                    Thread.sleep(5000);
                    pointer = 0;
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }

    public void stop() {
        group.shutdownGracefully();
    }
}
