package shibo.study.rpc.api;

/**
 * 关联一个服务
 *
 * @author zhangshibo
 */
public interface Referer {

    /**
     * 根据服务接口关联一个服务
     *
     * @param serviceInterface 服务接口
     * @param <T>              服务接口类型
     * @return 服务本地的动态代理
     */
    <T> T refer(Class<T> serviceInterface);
}
