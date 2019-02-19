package shibo.study.rpc.simple;

import shibo.study.rpc.api.Referer;

/**
 * @author zhangshibo
 */
public class RpcClient implements Referer {

    private String host;
    private int port;

    public RpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public <T> T refer(Class<T> serviceInterface) {
        return RpcFramework.refer(serviceInterface, host, port);
    }
}
