package shibo.study.rpc.netty;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangshibo
 */
@Data
public class RpcResponse implements Serializable {

    private String requestId;

    private Object result;

    private Throwable error;
}
