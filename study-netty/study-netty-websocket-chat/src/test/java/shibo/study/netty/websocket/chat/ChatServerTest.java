package shibo.study.netty.websocket.chat;

import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author zhangshibo
 */
@Slf4j
public class ChatServerTest {

    public static void main(String[] args) {
        if (args.length != 1) {
            log.error("Please give port as argument");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);

        ChatServer endpoint = new ChatServer();
        ChannelFuture f = endpoint.start(new InetSocketAddress(port));

        Runtime.getRuntime().addShutdownHook(new Thread(endpoint::destroy));
        f.channel().closeFuture().syncUninterruptibly();
    }
}
