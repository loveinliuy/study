package shibo.study.rpc.netty.server;

import lombok.extern.slf4j.Slf4j;
import shibo.study.rpc.netty.RpcRequest;
import shibo.study.rpc.netty.RpcResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author zhangshibo
 */
@Slf4j
public class RpcInvokerListener {

    public RpcResponse invoke(RpcRequest request) {
        Object serviceBean = RpcServiceRegistration.getBean(request.getClassName());

        RpcResponse response = new RpcResponse();
        response.setRequestId(request.getRequestId());
        try {
            Method method = serviceBean.getClass().getMethod(request.getMethodName(), request.getParameterTypes());
            Object result = method.invoke(serviceBean, request.getParameters());
            response.setResult(result);
        } catch (Throwable e) {
            if (e instanceof InvocationTargetException) {
                InvocationTargetException invocationTargetException = (InvocationTargetException) e;
                Throwable targetException = invocationTargetException.getTargetException();
                log.debug("error while do invoke. cause by: {}", targetException.getMessage());
                response.setError(targetException);
            } else {
                log.error(e.getMessage(), e);
                response.setError(new IllegalStateException("An error occurred at server."));
            }
        }
        return response;
    }
}
