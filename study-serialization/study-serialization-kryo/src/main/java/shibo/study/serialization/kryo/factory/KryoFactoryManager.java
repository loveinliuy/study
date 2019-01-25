package shibo.study.serialization.kryo.factory;

/**
 * @author zhangshibo
 */
public final class KryoFactoryManager {

    private KryoFactoryManager() {
    }

    private static final AbstractKryoFactory PROTOTYPE = new PrototypeKryoFactory();

    private static final AbstractKryoFactory POOLED = new PooledKryoFactory();

    private static final AbstractKryoFactory THREAD_LOCAL = new ThreadLocalKryoFactory();

    public static AbstractKryoFactory getDefault() {
        return THREAD_LOCAL;
    }

    public static AbstractKryoFactory getPrototype() {
        return PROTOTYPE;
    }

    public static AbstractKryoFactory getPooled() {
        return POOLED;
    }

    public static AbstractKryoFactory getThreadLocal() {
        return THREAD_LOCAL;
    }
}
