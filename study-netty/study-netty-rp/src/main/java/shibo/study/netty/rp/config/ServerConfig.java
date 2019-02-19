package shibo.study.netty.rp.config;

import lombok.Builder;
import lombok.Getter;

/**
 * @author zhangshibo
 */
@Builder
@Getter
public class ServerConfig {

    private int port;

    private int parentGroupThreads;

    private int childGroupThreads;

    private ProxyConfig proxy;
}
