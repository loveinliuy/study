package shibo.study.serialization;

import shibo.study.serialization.protostuff.ProtostuffSerializer;

/**
 * @author zhangshibo
 */
public class ProtostuffSerializerTest extends AbstractSerializerTest {

    {
        super.serializer = new ProtostuffSerializer();
    }
}
