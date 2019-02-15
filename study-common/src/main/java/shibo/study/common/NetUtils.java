package shibo.study.common;

import org.apache.commons.lang3.RandomUtils;

import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * @author zhangshibo
 */
public final class NetUtils {

    private NetUtils() {
    }

    public static int getRandomAvaliablePort() {
        final int maxTestCount = 10;
        int attempts = 0;
        DatagramSocket socket = null;
        while (attempts < maxTestCount) {
            try {
                int port = RandomUtils.nextInt(5001, 65536);
                socket = new DatagramSocket(port);
                return port;
            } catch (SocketException ignore) {
            } finally {
                if (socket != null) {
                    socket.close();
                }
            }
            attempts++;
        }
        throw new IllegalStateException("Can not get a avaliable port after attempt for " + maxTestCount + " times");
    }
}
