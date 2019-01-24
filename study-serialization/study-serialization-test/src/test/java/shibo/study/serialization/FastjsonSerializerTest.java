package shibo.study.serialization;

import shibo.study.serialization.fastjson.FastjsonSerializer;

/**
 * @author zhangshibo
 */
public class FastjsonSerializerTest extends AbstractSerializerTest {

    {
        super.serializer = new FastjsonSerializer();
    }
}
