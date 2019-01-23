package shibo.study.proxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
        TestService target = new TestServiceImpl();
        TestService proxy = factory.getProxy(target, new InvocationHandler() {

            private Log log = LogFactory.getLog("test.javassist.handler");

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String methodName = method.getName();
                log.debug("[before] The Method " + methodName + " begins");
                Object result = null;
                try {
                    result = method.invoke(target, args);
                    log.debug("[after] The Method " + methodName + " done successfully. result: " + result);
                } catch (Exception e) {
                    log.error("error while execute " + methodName, e);
                }
                return result;
            }
        });

        proxy.say("hello");
        String result = proxy.echo("world");
        assertEquals("world", result);
    }

    @Test
    public void proxyInterfaceTest() {
        logger.info("using proxy factory: {}", factory);
        TestService proxy = factory.getInterfaceProxy(TestService.class, new InvocationHandler() {

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
