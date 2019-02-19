package shibo.study.rpc.simple;

import shibo.study.rpc.api.Exporter;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangshibo
 */
public class RpcServer implements Exporter {

    private int port;

    private ExecutorService executorService = new ThreadPoolExecutor(1, 1, 0L,
            TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(1), r -> new Thread(r, "RpcServer runner"));

    public RpcServer(int port) {
        this.port = port;
    }

    @Override
    public void export(Object source) {
        executorService.execute(() -> RpcFramework.export(source, port));
    }

    public void shutdown() {
        executorService.shutdownNow();
    }

}
