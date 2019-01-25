package shibo.study.serialization.kryo.factory;

import com.esotericsoftware.kryo.Kryo;

/**
 * @author zhangshibo
 */
class PrototypeKryoFactory extends AbstractKryoFactory {

    @Override
    public Kryo getKryo() {
        return create();
    }

    @Override
    public void returnKryo(Kryo kryo) {

    }
}
