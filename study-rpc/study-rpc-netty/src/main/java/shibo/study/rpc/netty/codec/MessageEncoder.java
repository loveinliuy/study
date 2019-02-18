package shibo.study.rpc.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import shibo.study.serialization.Serializer;

/**
 * @author zhangshibo
 */
public class MessageEncoder<T> extends MessageToByteEncoder<Object> {

    private final Serializer serializer;
    private final Class<T> genericClass;

    public MessageEncoder(Class<T> genericClass, Serializer serializer) {
        this.genericClass = genericClass;
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (genericClass.isInstance(msg)) {
            byte[] data = serializer.serialize(msg);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}
