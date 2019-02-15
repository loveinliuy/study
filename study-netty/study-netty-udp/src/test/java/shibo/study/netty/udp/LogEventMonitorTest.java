package shibo.study.netty.udp;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import shibo.study.netty.udp.monitor.LogEventMonitor;

import java.net.InetSocketAddress;

/**
 * @author zhangshibo
 */
@Slf4j
public class LogEventMonitorTest {

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 1) {
            log.error("Usage: LogEventMonitor <port>");
            System.exit(1);
        }

        LogEventMonitor monitor = new LogEventMonitor(new InetSocketAddress(Integer.parseInt(args[0])));
        try {
            Channel channel = monitor.bind();

            log.info("LogEventMonitor running");
            channel.closeFuture().await();
        } finally {
            monitor.stop();
        }
    }
}
