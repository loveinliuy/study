package shibo.study.serialization.kryo.factory;

/**
 * @author zhangshibo
 */
public class PooledKryoFactoryTests extends AbstractKryoFactoryTests {

    {
        super.factory = KryoFactoryManager.getPooled();
    }
}
