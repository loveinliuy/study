package shibo.study.serialization.kryo.factory;

import com.esotericsoftware.kryo.Kryo;

/**
 * @author zhangshibo
 */
class ThreadLocalKryoFactory extends AbstractKryoFactory {

    private final ThreadLocal<Kryo> holder = ThreadLocal.withInitial(this::create);

    @Override
    public Kryo getKryo() {
        return holder.get();
    }

    @Override
    public void returnKryo(Kryo kryo) {

    }
}
