package shibo.study.netty.httproxy;

import lombok.Builder;
import lombok.Getter;

/**
 * @author zhangshibo
 */
@Builder
@Getter
public class ProxyConfig {

    private int port;

    private int parentGroupThreads;

    private int childGroupThreads;

    private int proxyGroupThreads;
}
