package shibo.study.proxy;

/**
 * @author zhangshibo
 */
public class JdkProxyTest extends AbstractProxyTest {

    {
        factory = ProxyFactoryManager.jdkProxyFactory();
    }
}
