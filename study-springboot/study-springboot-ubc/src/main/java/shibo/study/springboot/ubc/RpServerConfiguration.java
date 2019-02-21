package shibo.study.springboot.ubc;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author zhangshibo
 */
@Configuration
@EnableConfigurationProperties(RpServerProperties.class)
public class RpServerConfiguration {

    @Bean(value = "rpServer", initMethod = "init", destroyMethod = "destroy")
    public RpServerBean rpServer(RpServerProperties prop) throws URISyntaxException {
        URI loaderUri = new URI("http://172.18.32.134:8080/loader.js");
        return new RpServerBean(loaderUri, prop);
    }
}
