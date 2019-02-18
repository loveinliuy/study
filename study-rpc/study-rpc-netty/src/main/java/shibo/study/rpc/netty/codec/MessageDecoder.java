package shibo.study.rpc.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import shibo.study.serialization.Serializer;

import java.util.List;

/**
 * @author zhangshibo
 */
@Slf4j
public class MessageDecoder<T> extends ByteToMessageDecoder {

    private final Class<T> genericClass;

    private final Serializer serializer;

    public MessageDecoder(Class<T> genericClass, Serializer serializer) {
        this.genericClass = genericClass;
        this.serializer = serializer;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        in.markReaderIndex();
        int dataLen = in.readInt();
        if (in.readableBytes() < dataLen) {
            log.debug("Got expect len: {} but {} actual.", dataLen, in.readableBytes());
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLen];
        in.readBytes(data);
        Object o = serializer.deserialize(data, genericClass);
        out.add(o);
    }
}
