package shibo.study.proxy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import shibo.study.proxy.javassist.JavassistProxyFactory;

import static org.junit.Assert.assertEquals;

/**
 * @author zhangshibo
 */
abstract class AbstractProxyTest {


    private Logger logger = LogManager.getLogger();

    ProxyFactory factory;

    @Test
    public void proxyTest() {
        logger.info("using proxy factory: {}", factory);
        TestService target = new TestServiceImpl();
        ProxyFactory factory = new JavassistProxyFactory();
        TestService proxy = factory.getProxy(target, new TestInvocationHandler(target));

        proxy.say("hello");
        String result = proxy.echo("world");
        assertEquals("world", result);
    }
}
