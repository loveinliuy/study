package shibo.study.rpc.netty.client;

import shibo.study.rpc.netty.RpcRequest;
import shibo.study.rpc.netty.RpcResponse;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangshibo
 */
public class RpcCallback {

    public static ConcurrentHashMap<String, RpcCallback> responsePool =
            new ConcurrentHashMap<>();

    private RpcRequest request;
    private RpcResponse response;

    private volatile boolean isDone = false;
    private final Object lock = new Object();

    public RpcCallback(RpcRequest request) {
        this.request = request;
        responsePool.put(request.getRequestId(), this);
    }

    public void setResponse(RpcResponse response) {
        this.response = response;
        synchronized (lock) {
            isDone = true;
            lock.notifyAll();
        }
    }

    public RpcResponse get(long timeoutMills) throws InterruptedException {
        if (!isDone) {
            synchronized (lock) {
                lock.wait(timeoutMills);
            }
        }
        if (!isDone) {
            throw new IllegalStateException("timeout while get response from rpc server.");
        }
        return response;
    }
}
