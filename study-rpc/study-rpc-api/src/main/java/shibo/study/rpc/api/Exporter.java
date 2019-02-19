package shibo.study.rpc.api;

/**
 * @author zhangshibo
 */
public interface Exporter {

    /**
     * 暴露一个服务
     *
     * @param source 服务的实现
     */
    void export(Object source);
}
