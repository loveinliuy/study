package shibo.study.serialization.kryo.factory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

/**
 * @author zhangshibo
 */
@Log4j2
public abstract class AbstractKryoFactoryTests {

    AbstractKryoFactory factory;

    private ExecutorService executorService;

    private Runnable worker;

    @Before
    public void before() {
        Objects.requireNonNull(factory);
        TestBean bean = new TestBean();
        bean.setId(RandomStringUtils.randomAlphabetic(20));
        bean.setKey(RandomUtils.nextLong());
        factory.registerClass(TestBean.class);

        final int thread = 20;
        int wait = 200;
        executorService = new ThreadPoolExecutor(thread, thread, 1L, TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(wait), new ThreadFactory() {
            private AtomicInteger counter = new AtomicInteger();

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "test-kryo-factory-" + counter.incrementAndGet());
            }
        });

        final int count = 200;
        worker = () -> {
            for (int i = 0; i < count; i++) {
                Kryo kryo = factory.getKryo();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                Output output = new Output(bos);
                kryo.writeObject(output, bean);
                output.close();
                byte[] data = bos.toByteArray();
                factory.returnKryo(kryo);

                kryo = factory.getKryo();
                ByteArrayInputStream bis = new ByteArrayInputStream(data);
                Input input = new Input(bis);
                TestBean res = kryo.readObject(input, TestBean.class);
                input.close();

                assertEquals(bean, res);
            }
        };
    }

    @Test
    public void multiThreadTest() throws ExecutionException, InterruptedException {
        final int concurrent = 200;
        List<Future<?>> list = new LinkedList<>();
        long start = System.currentTimeMillis();
        for (int i = 0; i < concurrent; i++) {
            list.add(executorService.submit(worker));
        }
        for (Future<?> f : list) {
            f.get();
        }
        long end = System.currentTimeMillis();
        log.info("测试工厂：{}，序列化任务数量：{}，耗时：{}ms", factory.getClass().getName(), concurrent, end - start);
    }

    @After
    public void after() {
        executorService.shutdown();
    }

}
