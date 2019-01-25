package shibo.study.serialization.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import shibo.study.serialization.Serializer;
import shibo.study.serialization.kryo.factory.AbstractKryoFactory;
import shibo.study.serialization.kryo.factory.KryoFactoryManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author zhangshibo
 */
public class KryoSerializer implements Serializer {

    private AbstractKryoFactory factory;

    public KryoSerializer() {
        this(KryoFactoryManager.getDefault());
    }

    public KryoSerializer(AbstractKryoFactory factory) {
        this.factory = factory;
    }


    @Override
    public <T> byte[] serialize(T obj) {
        Kryo kryo = null;
        try {
            kryo = factory.getKryo();
            kryo.register(obj.getClass());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Output output = new Output(bos);
            kryo.writeObject(output, obj);
            output.close();
            return bos.toByteArray();
        } finally {
            if (kryo != null) {
                factory.returnKryo(kryo);
            }
        }
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> cls) {
        Kryo kryo = null;
        try {
            kryo = factory.getKryo();
            kryo.register(cls);
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            Input input = new Input(bis);
            return kryo.readObject(input, cls);
        } finally {
            if (kryo != null) {
                factory.returnKryo(kryo);
            }
        }
    }
}
