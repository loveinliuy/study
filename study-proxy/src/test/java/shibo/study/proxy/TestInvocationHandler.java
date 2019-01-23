package shibo.study.proxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author zhangshibo
 */
public class TestInvocationHandler implements InvocationHandler {

    private final Object target;

    TestInvocationHandler(Object target) {
        this.target = target;
    }

    private Log log = LogFactory.getLog("test.javassist.handler");

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
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


}
