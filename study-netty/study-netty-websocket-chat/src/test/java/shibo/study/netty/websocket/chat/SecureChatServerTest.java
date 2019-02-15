package shibo.study.netty.websocket.chat;

import io.netty.channel.ChannelFuture;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLException;
import java.net.InetSocketAddress;
import java.security.cert.CertificateException;

/**
 * @author zhangshibo
 */
@Slf4j
public class SecureChatServerTest {

    public static void main(String[] args) throws CertificateException, SSLException {
        if (args.length != 1) {
            log.error("Usage: SecureChatServer <port>");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);
        SelfSignedCertificate cert = new SelfSignedCertificate();
        SslContext context = SslContextBuilder.forServer(cert.certificate(), cert.privateKey()).build();

        SecureChatServer endpoint = new SecureChatServer(context);
        ChannelFuture f = endpoint.start(new InetSocketAddress(port));
        Runtime.getRuntime().addShutdownHook(new Thread(endpoint::destroy));
        f.channel().closeFuture().syncUninterruptibly();
    }
}
