package shibo.study.serialization;

import shibo.study.serialization.kryo.KryoSerializer;

/**
 * @author zhangshibo
 */
public class KryoSerializerTest extends AbstractSerializerTest {

    {
        super.serializer = new KryoSerializer();
    }
}
