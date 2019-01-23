package shibo.study.proxy;

import java.lang.reflect.InvocationHandler;

/**
 * @author zhangshibo
 */
public interface ProxyFactory {

    /**
     * 获取一个类的动态代理
     *
     * @param target  动态代理目标
     * @param handler 动态代理的处理
     * @param <T>     代理的结果类型
     * @return 经过动态代理的类
     * @throws IllegalArgumentException 当传入参数不正确的时候抛出
     */
    <T> T getProxy(Object target, InvocationHandler handler) throws IllegalArgumentException;
}
