package shibo.study.springboot.ubc;

import lombok.Data;
import lombok.ToString;

/**
 * @author zhangshibo
 */
@Data
@ToString
public class UserBehaviourInfo {

    private String title;

    private String referrer;

    private String ua;

    private String url;

    private Integer sh;

    private Integer sw;

    private Integer cd;

    private String lang;

    // aggregated by server

    private String remoteHost;

    private Integer remotePort;
}
