package shibo.study.proxy.jdk;

import shibo.study.proxy.ProxyFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Objects;

/**
 * @author zhangshibo
 */
public class JdkProxyFactory implements ProxyFactory {


    @Override
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<?> targetInterface, InvocationHandler handler) throws IllegalArgumentException {
        Objects.requireNonNull(targetInterface, "target can not be null!");
        if (!targetInterface.isInterface()) {
            throw new IllegalArgumentException("target must be a interface");
        }
        return (T) Proxy.newProxyInstance(targetInterface.getClassLoader(), new Class[]{targetInterface}, handler);
    }
}
