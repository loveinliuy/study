package shibo.study.proxy;

import shibo.study.proxy.javassist.JavassistProxyFactory;

/**
 * @author zhangshibo
 */
public class JavassistProxyTest extends AbstractProxyTest {

    {
        factory = new JavassistProxyFactory();
    }
}
