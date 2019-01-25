package shibo.study.serialization.hessian;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import shibo.study.serialization.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author zhangshibo
 */
public class HessianSerializer implements Serializer {

    private static SerializerFactory factory = new SerializerFactory();

    @Override
    public <T> byte[] serialize(T obj) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Hessian2Output out = new Hessian2Output(bos);
        out.setSerializerFactory(factory);
        try {
            out.writeObject(obj);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            try {
                out.close();
            } catch (IOException ignored) {
            }
        }
        return bos.toByteArray();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(byte[] data, Class<T> cls) {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        Hessian2Input input = new Hessian2Input(bis);
        input.setSerializerFactory(factory);
        try {
            Object tmp = input.readObject();
            if (!tmp.getClass().equals(cls)) {
                throw new IllegalArgumentException(
                        String.format("Data class %s is not valid to %s", tmp.getClass().getName(), cls.getName()));
            }
            return (T) tmp;
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            try {
                input.close();
            } catch (IOException ignored) {
            }
        }
    }
}
