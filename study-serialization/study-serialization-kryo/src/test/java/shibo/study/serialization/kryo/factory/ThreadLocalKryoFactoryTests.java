package shibo.study.serialization.kryo.factory;

/**
 * @author zhangshibo
 */
public class ThreadLocalKryoFactoryTests extends AbstractKryoFactoryTests {
    {
        super.factory = KryoFactoryManager.getThreadLocal();
    }

}
