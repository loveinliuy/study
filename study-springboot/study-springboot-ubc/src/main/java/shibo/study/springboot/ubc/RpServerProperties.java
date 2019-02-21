package shibo.study.springboot.ubc;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhangshibo
 */
@Data
@ConfigurationProperties(prefix = "ubc.rp")
public class RpServerProperties {

    private int port;

    private int parentGroupThreads = 1;

    private int workerGroupThreads = 0;

    private String proxyHost;

    private int proxyPort;

}
