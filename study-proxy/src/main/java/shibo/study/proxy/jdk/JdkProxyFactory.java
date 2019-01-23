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
    public <T> T getProxy(Object target, InvocationHandler handler) throws IllegalArgumentException {
        Objects.requireNonNull(target, "target can not be null!");
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{target.getClass()}, handler);
    }
}
