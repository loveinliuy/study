package shibo.study.proxy;

/**
 * @author zhangshibo
 */
public class JavassistProxyTest extends AbstractProxyTest {

    {
        factory = ProxyFactoryManager.javassistProxyFactory();
    }
}
