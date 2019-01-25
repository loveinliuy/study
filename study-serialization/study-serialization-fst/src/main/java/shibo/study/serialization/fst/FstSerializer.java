package shibo.study.serialization.fst;

import org.nustaq.serialization.FSTConfiguration;
import shibo.study.serialization.Serializer;

/**
 * @author zhangshibo
 */
public class FstSerializer implements Serializer {

    private static FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();

    @Override
    public <T> byte[] serialize(T obj) {
        return conf.asByteArray(obj);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(byte[] data, Class<T> cls) {
        Object obj = conf.asObject(data);
        if (!obj.getClass().equals(cls)) {
            throw new IllegalArgumentException(
                    String.format("Target class is %s but given class is %s", cls.getName(), obj.getClass().getName()));
        }
        return (T) obj;
    }
}
