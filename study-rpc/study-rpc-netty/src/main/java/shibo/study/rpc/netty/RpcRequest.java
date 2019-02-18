package shibo.study.rpc.netty;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangshibo
 */
@Data
public class RpcRequest implements Serializable {

    private String requestId;

    private long createTimeInMills;

    private String className;

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] parameters;
}
