package shibo.study.netty.spdy;

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
public class SpdyServerTest {

    public static void main(String[] args) throws SSLException, CertificateException {
        if (args.length != 1) {
            log.error("Usage: SpdyServer <port>");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);

        SelfSignedCertificate cert = new SelfSignedCertificate();
        SslContext context = SslContextBuilder.forServer(cert.certificate(), cert.privateKey()).build();

        SpdyServer endpoint = new SpdyServer(context);
        ChannelFuture f = endpoint.start(new InetSocketAddress(port));

        Runtime.getRuntime().addShutdownHook(new Thread(endpoint::destroy));
        f.channel().closeFuture().syncUninterruptibly();
    }
}
