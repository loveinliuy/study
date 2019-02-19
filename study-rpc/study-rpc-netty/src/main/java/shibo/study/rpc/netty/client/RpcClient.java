package shibo.study.rpc.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import shibo.study.rpc.api.Referer;
import shibo.study.rpc.netty.RpcRequest;
import shibo.study.rpc.netty.RpcResponse;
import shibo.study.rpc.netty.codec.MessageDecoder;
import shibo.study.rpc.netty.codec.MessageEncoder;

import java.util.UUID;

/**
 * @author zhangshibo
 */
@Slf4j
public class RpcClient implements Referer {

    private final ClientConfig config;

    private final EventLoopGroup group = new NioEventLoopGroup();
    private Channel channel;

    public RpcClient(ClientConfig config) {
        this.config = config;
    }

    public void start() {
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new MessageEncoder<>(RpcRequest.class, config.getSerializer()))
                                .addLast(new MessageDecoder<>(RpcResponse.class, config.getSerializer()))
                                .addLast(new RpcClientHandler());
                    }
                });
        ChannelFuture f = b.connect(config.getServerHost(), config.getServerPort()).syncUninterruptibly();
        log.info("rpc client start and connected to {}:{}", config.getServerHost(), config.getServerPort());
        this.channel = f.channel();
    }

    @Override
    public <T> T refer(Class<T> serviceInterface) {
        return config.getProxyFactory().getProxy(serviceInterface, (proxy, method, args) ->
                invoke0(method.getDeclaringClass().getName(), method.getName(),
                        method.getParameterTypes(), args));
    }

    private Object invoke0(String className, String methodName, Class<?>[] parameterTypes, Object[] parameters)
            throws Throwable {
        RpcRequest request = new RpcRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setCreateTimeInMills(System.currentTimeMillis());
        request.setClassName(className);
        request.setMethodName(methodName);
        request.setParameters(parameters);
        request.setParameterTypes(parameterTypes);

        RpcCallback callback = new RpcCallback(request);
        RpcCallback.responsePool.put(request.getRequestId(), callback);

        channel.writeAndFlush(request);

        RpcResponse response = callback.get(config.getTimeoutMills());
        if (response.getError() != null) {
            throw response.getError();
        } else {
            return response.getResult();
        }
    }

    public void shutdown() {
        if (this.channel != null) {
            channel.close().syncUninterruptibly();
        }
        group.shutdownGracefully();
    }
}
