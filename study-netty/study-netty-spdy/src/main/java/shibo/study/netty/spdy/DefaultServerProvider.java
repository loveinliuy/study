package shibo.study.netty.spdy;

import org.eclipse.jetty.npn.NextProtoNego;

import java.util.Arrays;
import java.util.List;

/**
 * @author zhangshibo
 */
public class DefaultServerProvider implements NextProtoNego.ServerProvider {

    private static final List<String> PROTOCOLS =
            Arrays.asList("spdy/2", "spdy/3", "http/1.1");

    private String protocol;

    @Override
    public void unsupported() {
        protocol = "http/1.1";
    }

    @Override
    public List<String> protocols() {
        return PROTOCOLS;
    }

    @Override
    public void protocolSelected(String protocol) {
        this.protocol = protocol;
    }
}
