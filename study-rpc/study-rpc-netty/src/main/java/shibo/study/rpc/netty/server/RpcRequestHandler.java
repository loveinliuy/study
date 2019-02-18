package shibo.study.rpc.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import shibo.study.rpc.netty.RpcRequest;
import shibo.study.rpc.netty.RpcResponse;

/**
 * @author zhangshibo
 */
@Slf4j
public class RpcRequestHandler extends SimpleChannelInboundHandler<RpcRequest> {

    public RpcRequestHandler(RpcInvokerListener invokerListener) {
        this.invokerListener = invokerListener;
    }

    private final RpcInvokerListener invokerListener;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        RpcResponse response = invokerListener.invoke(msg);
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("error while execute.", cause);
        ctx.close();
    }
}
