package shibo.study.serialization;

import shibo.study.serialization.fst.FstSerializer;

/**
 * @author zhangshibo
 */
public class FstSerializerTest extends AbstractSerializerTest {

    {
        super.serializer = new FstSerializer();
    }
}
