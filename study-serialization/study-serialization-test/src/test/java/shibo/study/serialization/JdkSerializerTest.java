package shibo.study.serialization;

import shibo.study.serialization.jdk.JdkSerializer;

/**
 * @author zhangshibo
 */
public class JdkSerializerTest extends AbstractSerializerTest {

    {
        serializer = new JdkSerializer();
    }
}
