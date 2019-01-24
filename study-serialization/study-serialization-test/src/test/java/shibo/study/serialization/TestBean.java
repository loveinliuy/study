package shibo.study.serialization;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author zhangshibo
 */
@Data
class TestBean implements Serializable {

    private long id;

    private String key;

    private Date date;

    private List<TestBean> inner;
}
