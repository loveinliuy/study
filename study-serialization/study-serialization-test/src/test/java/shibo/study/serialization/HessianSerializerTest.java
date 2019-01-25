package shibo.study.serialization;

import shibo.study.serialization.hessian.HessianSerializer;

/**
 * @author zhangshibo
 */
public class HessianSerializerTest extends AbstractSerializerTest {

    {
        super.serializer = new HessianSerializer();
    }
}
