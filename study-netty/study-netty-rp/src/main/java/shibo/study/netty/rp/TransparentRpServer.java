package shibo.study.netty.rp;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import shibo.study.netty.rp.config.ServerConfig;
import shibo.study.netty.rp.handler.TransparentFrontendHandler;

/**
 * @author zhangshibo
 */
@Slf4j
public class TransparentRpServer extends AbstractRpServer {


    public TransparentRpServer(ServerConfig config) {
        super(config);
    }


    @Override
    protected ChannelHandler createChildChannelHandler(ServerConfig config) {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline()
                        .addLast(new LoggingHandler())
                        .addLast(new HttpServerCodec())
                        .addLast(new TransparentFrontendHandler(config.getProxy()));
            }
        };
    }

}
