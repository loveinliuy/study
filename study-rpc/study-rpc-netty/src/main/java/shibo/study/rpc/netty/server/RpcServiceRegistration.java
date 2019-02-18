package shibo.study.rpc.netty.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangshibo
 */
public class RpcServiceRegistration {

    public static final Map<String, Object> SERVICE = new ConcurrentHashMap<>();

    public static void register(Object serviceBean) {
        Class<?>[] interfaces = serviceBean.getClass().getInterfaces();
        if (interfaces.length == 0) {
            throw new IllegalArgumentException("Service Bean must be a implementation of a interface!");
        }
        if (interfaces.length != 1) {
            throw new IllegalArgumentException("Service Bean must only implements 1 interface!");
        }
        SERVICE.put(interfaces[0].getName(), serviceBean);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        if (SERVICE.containsKey(beanName)) {
            return (T) SERVICE.get(beanName);
        } else {
            throw new IllegalStateException("No such service bean named: " + beanName);
        }
    }
}
