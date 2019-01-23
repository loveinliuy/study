package shibo.study.rpc.framework.simple;

/**
 * @author zhangshibo
 */
public class RpcServer {

    public static void main(String[] args) {
        TestService service = new TestServiceImpl();
        RpcFramework.export(service, 1234);
    }
}
