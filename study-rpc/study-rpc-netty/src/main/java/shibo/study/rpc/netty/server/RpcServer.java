package shibo.study.rpc.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import shibo.study.rpc.api.Exporter;
import shibo.study.rpc.netty.RpcRequest;
import shibo.study.rpc.netty.RpcResponse;
import shibo.study.rpc.netty.codec.MessageDecoder;
import shibo.study.rpc.netty.codec.MessageEncoder;

/**
 * @author zhangshibo
 */
@Slf4j
public class RpcServer implements Exporter {

    private final ServerConfig config;
    private final EventLoopGroup parent = new NioEventLoopGroup();
    private final EventLoopGroup child = new NioEventLoopGroup();

    private Channel channel;

    public RpcServer(ServerConfig config) {
        this.config = config;
    }

    public void start() {
        ServerBootstrap b = new ServerBootstrap();
        b.group(parent, child)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new MessageDecoder<>(RpcRequest.class, config.getSerializer()))
                                .addLast(new MessageEncoder<>(RpcResponse.class, config.getSerializer()))
                                .addLast(new RpcRequestHandler(new RpcInvokerListener()));
                    }
                });

        ChannelFuture f = b.bind(config.getPort()).syncUninterruptibly();
        log.info("RpcServer start listening port: {}", config.getPort());
        channel = f.channel();
    }

    @Override
    public void export(Object service) {
        RpcServiceRegistration.register(service);
    }

    public void shutdown() {
        if (channel != null) {
            channel.close().syncUninterruptibly();
        }
        child.shutdownGracefully();
        parent.shutdownGracefully();
    }
}
