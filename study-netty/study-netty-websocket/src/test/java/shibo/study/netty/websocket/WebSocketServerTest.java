package shibo.study.netty.websocket;

import shibo.study.netty.websocket.server.WebSocketServer;

/**
 * @author zhangshibo
 */
public class WebSocketServerTest {

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 1) {
            throw new IllegalArgumentException("Usage: WebSocketServer <port>");
        }
        int port = Integer.parseInt(args[0]);
        WebSocketServer server = new WebSocketServer(port);
        server.start();
    }
}
