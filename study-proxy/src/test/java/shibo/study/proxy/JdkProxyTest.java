package shibo.study.proxy;

import shibo.study.proxy.jdk.JdkProxyFactory;

/**
 * @author zhangshibo
 */
public class JdkProxyTest extends AbstractProxyTest {

    {
        factory = new JdkProxyFactory();
    }
}
