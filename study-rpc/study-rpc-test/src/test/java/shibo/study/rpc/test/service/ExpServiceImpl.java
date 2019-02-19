package shibo.study.rpc.test.service;

/**
 * @author zhangshibo
 */
public class ExpServiceImpl implements ExpService {
    @Override
    public long exp(int base, int exp) {
        if (exp < 0) {
            throw new IllegalArgumentException("exp " + exp + " must not be negative");
        }
        long res = 1;
        for (int i = 0; i < exp; i++) {
            res *= base;
        }
        return res;
    }
}
