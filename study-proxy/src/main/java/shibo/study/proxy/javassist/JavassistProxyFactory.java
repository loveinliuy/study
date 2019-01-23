package shibo.study.proxy.javassist;

import shibo.study.proxy.ProxyFactory;

import java.lang.reflect.InvocationHandler;
import java.util.Objects;

/**
 * @author zhangshibo
 */
public class JavassistProxyFactory implements ProxyFactory {


    @Override
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<?> targetInterface, InvocationHandler handler) throws IllegalArgumentException {
        Objects.requireNonNull(targetInterface, "target can not be null!");
        try {
            return (T) ProxyGenerator.newProxyInstance(targetInterface.getClassLoader(), targetInterface, handler);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
