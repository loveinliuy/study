package shibo.study.serialization;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

/**
 * @author zhangshibo
 */
public abstract class AbstractSerializerTest {

    private Logger logger = LogManager.getLogger();

    protected Serializer serializer;


    @Before
    public void init() {
        Objects.requireNonNull(serializer);

    }

    @Test
    public void testSerializationAndDeserialization() {
        TestBean testBean = new TestBean();
        List<TestBean> inner = new ArrayList<>();
        TestBean cons1 = new TestBean();
        cons1.setId(RandomUtils.nextLong());
        cons1.setDate(Date.from(LocalDateTime.now()
                .minusDays(RandomUtils.nextInt(5, 10))
                .atZone(ZoneId.systemDefault()).toInstant()));
        cons1.setKey(RandomStringUtils.randomAlphabetic(5));
        inner.add(cons1);

        testBean.setId(RandomUtils.nextLong());
        testBean.setDate(new Date());
        testBean.setKey(RandomStringUtils.randomAlphabetic(10));
        testBean.setInner(inner);
        long start = System.currentTimeMillis();
        int count = RandomUtils.nextInt(10, 100);
        long totalSize = 0;
        for (int i = 0; i < count; i++) {
            byte[] data = serializer.serialize(testBean);
            totalSize += data.length;
            TestBean obj = serializer.deserialize(data, TestBean.class);
            assertEquals(testBean, obj);
        }
        long end = System.currentTimeMillis();
        logger.info("使用{}，序列化/反序列化次数：{}， 用时：{}ms, 使用空间大小：{}b",
                serializer.getClass().getName(), count, end - start, totalSize);
    }

}
