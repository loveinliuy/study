package shibo.study.netty.rp;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhangshibo
 */
@Slf4j
public class ConcurrentTest4RpServer {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService service = new ThreadPoolExecutor(100, 100, 100, TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(2000), new ThreadFactory() {
            private AtomicInteger i = new AtomicInteger();

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "concurrent-test-worker-" + i.incrementAndGet());
            }
        });

        AtomicInteger count = new AtomicInteger();
        Runnable r = () -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://localhost:1234/hynw/")
                    .build();
            try (Response response = client.newCall(request).execute()) {
                log.debug("response: {}", response.isSuccessful());
                count.incrementAndGet();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        };

        List<Future> list = new LinkedList<>();
        for (int i = 0; i < 1000; i++) {
            Future f = service.submit(r);
            list.add(f);
        }

        for (Future f : list) {
            f.get();
        }

        log.info("total count: {}", count.get());
        System.exit(0);
    }
}
