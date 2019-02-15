package shibo.study.netty.udp;

import lombok.extern.slf4j.Slf4j;
import shibo.study.netty.udp.broadcaster.LogEventBroadcaster;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author zhangshibo
 */
@Slf4j
public class LogEventBroadcasterTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length != 2) {
            log.error("Usage: LogEventBroadcaster <port> <file>");
            System.exit(1);
        }
        LogEventBroadcaster broadcaster = new LogEventBroadcaster(
                new InetSocketAddress("255.255.255.255", Integer.parseInt(args[0])),
                new File(args[1]));

        try {
            broadcaster.run();
        } finally {
            broadcaster.stop();
        }
    }
}
