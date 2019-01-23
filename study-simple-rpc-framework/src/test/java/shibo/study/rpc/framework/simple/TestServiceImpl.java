package shibo.study.rpc.framework.simple;

/**
 * @author zhangshibo
 */
public class TestServiceImpl implements TestService {
    @Override
    public String hello(String name) {
        return "hello, " + name;
    }
}
