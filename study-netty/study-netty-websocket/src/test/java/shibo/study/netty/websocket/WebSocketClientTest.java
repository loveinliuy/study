package shibo.study.netty.websocket;

import org.apache.commons.lang3.RandomStringUtils;
import shibo.study.netty.websocket.client.WebSocketClient;

import java.io.FileInputStream;

/**
 * @author zhangshibo
 */
public class WebSocketClientTest {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            throw new IllegalArgumentException("Usage: WebSocketClient <uri>");
        }
        WebSocketClient client = new WebSocketClient(args[0]);
        client.start();
        client.sendTxt(RandomStringUtils.randomAlphabetic(10));
        client.sendBinary(new FileInputStream("/home/zhangshibo/icon.jpeg"));
        client.sendTxt(RandomStringUtils.randomAlphabetic(10));
        client.close();
    }
}
