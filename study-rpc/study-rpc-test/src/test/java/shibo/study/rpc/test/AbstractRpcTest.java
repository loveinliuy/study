package shibo.study.rpc.test;

import org.apache.commons.lang3.RandomUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import shibo.study.rpc.api.Exporter;
import shibo.study.rpc.api.Referer;
import shibo.study.rpc.test.service.ExpService;
import shibo.study.rpc.test.service.ExpServiceImpl;
import shibo.study.rpc.test.service.FibService;
import shibo.study.rpc.test.service.FibServiceImpl;

/**
 * @author zhangshibo
 */
public abstract class AbstractRpcTest {

    protected FibService fibServiceReferred;

    protected ExpService expServiceReferred;

    protected final FibService fibServiceSource = new FibServiceImpl();

    protected final ExpService expServiceSource = new ExpServiceImpl();

    @Before
    public void before() {
        Exporter server = initServer();
        server.export(fibServiceSource);
        server.export(expServiceSource);
        Referer client = initClient();

        fibServiceReferred = client.refer(FibService.class);
        expServiceReferred = client.refer(ExpService.class);
    }

    public void testFib(boolean negative) {
        int n = RandomUtils.nextInt(0, 10);
        if (negative) {
            n = n - 5;
        }
        try {
            long res = fibServiceReferred.fib(n);
            Assert.assertEquals(fibServiceSource.fib(n), res);
        } catch (IllegalArgumentException e) {
            try {
                fibServiceSource.fib(n);
            } catch (IllegalArgumentException se) {
                Assert.assertEquals(e.getMessage(), se.getMessage());
            }
        }
    }

    public void testExp(boolean negative) {
        int base = RandomUtils.nextInt(0, 20);
        int exp = RandomUtils.nextInt(0, 10);
        if (negative) {
            base = base - 10;
            exp = exp - 5;
        }
        try {
            long res = expServiceReferred.exp(base, exp);
            Assert.assertEquals(expServiceSource.exp(base, exp), res);
        } catch (IllegalArgumentException e) {
            try {
                expServiceSource.exp(base, exp);
            } catch (IllegalArgumentException se) {
                Assert.assertEquals(e.getMessage(), se.getMessage());
            }
        }
    }

    @After
    public void after() {
        shutdownClientAndServer();
    }

    protected abstract Referer initClient();

    protected abstract Exporter initServer();

    protected abstract void shutdownClientAndServer();

}
