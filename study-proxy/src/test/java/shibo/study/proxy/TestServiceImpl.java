package shibo.study.proxy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author zhangshibo
 */
public class TestServiceImpl implements TestService {

    private Logger log = LogManager.getLogger();

    @Override
    public void say(String msg) {
        log.info("say: " + msg);
    }

    @Override
    public String echo(String msg) {
        log.debug("echo: " + msg);
        return msg;
    }
}
