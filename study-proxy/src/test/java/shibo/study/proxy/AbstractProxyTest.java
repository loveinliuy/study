package shibo.study.proxy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

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
        TestService proxy = factory.getProxy(TestService.class, new InvocationHandler() {

            private TestService service = new TestServiceImpl();

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return method.invoke(service, args);
            }
        });

        proxy.say("hello");
        String result = proxy.echo("world");
        assertEquals("world", result);
    }
}
