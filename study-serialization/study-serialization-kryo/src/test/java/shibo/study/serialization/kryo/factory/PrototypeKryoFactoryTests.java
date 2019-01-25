package shibo.study.serialization.kryo.factory;

/**
 * @author zhangshibo
 */
public class PrototypeKryoFactoryTests extends AbstractKryoFactoryTests {

    {
        super.factory = KryoFactoryManager.getPrototype();
    }
}
