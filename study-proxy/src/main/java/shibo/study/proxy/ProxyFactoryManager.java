package shibo.study.proxy;

import shibo.study.proxy.javassist.JavassistProxyFactory;
import shibo.study.proxy.jdk.JdkProxyFactory;

/**
 * @author zhangshibo
 */
public class ProxyFactoryManager {

    private static final ProxyFactory JDK = new JdkProxyFactory();

    private static final ProxyFactory JAVASSIST = new JavassistProxyFactory();

    public static ProxyFactory defaultProxyFactory() {
        return JAVASSIST;
    }

    public static ProxyFactory jdkProxyFactory() {
        return JDK;
    }

    public static ProxyFactory javassistProxyFactory() {
        return JAVASSIST;
    }
}
