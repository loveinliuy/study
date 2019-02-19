package shibo.study.rpc.test.service;

/**
 * @author zhangshibo
 */
public class FibServiceImpl implements FibService {
    @Override
    public long fib(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("request fib must be a positive");
        }
        if (n <= 2) {
            return 1L;
        }
        int n1 = 1, n2 = 1, sn = 0;
        for (int i = 0; i < n - 2; i++) {
            sn = n1 + n2;
            n1 = n2;
            n2 = sn;
        }
        return sn;
    }
}
