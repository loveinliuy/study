package shibo.study.serialization.kryo.factory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.pool.KryoPool;

/**
 * @author zhangshibo
 */
class PooledKryoFactory extends AbstractKryoFactory {

    private KryoPool pool = new KryoPool.Builder(this).softReferences().build();

    @Override
    public Kryo getKryo() {
        return pool.borrow();
    }

    @Override
    public void returnKryo(Kryo kryo) {
        pool.release(kryo);
    }
}
