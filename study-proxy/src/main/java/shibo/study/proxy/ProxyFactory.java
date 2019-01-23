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
     * @return 经过动态代理的对象
     * @throws IllegalArgumentException 当传入参数不正确的时候抛出
     */
    <T> T getProxy(Object target, InvocationHandler handler) throws IllegalArgumentException;

    /**
     * 获取一个接口的动态代理
     *
     * @param targetInterface 动态代理目标
     * @param handler         动态代理的处理对象
     * @param <T>             动态代理的结果类型
     * @return 经过动态代理的对象
     * @throws IllegalArgumentException 当传入内容非法时抛出
     */
    <T> T getInterfaceProxy(Class<?> targetInterface, InvocationHandler handler) throws IllegalArgumentException;
}
