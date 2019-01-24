package shibo.study.serialization.fastjson;

import com.alibaba.fastjson.JSON;
import shibo.study.serialization.Serializer;

import java.nio.charset.StandardCharsets;

/**
 * @author zhangshibo
 */
public class FastjsonSerializer implements Serializer {


    @Override
    public <T> byte[] serialize(T obj) {
        String json = JSON.toJSONString(obj);
        return json.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> cls) {
        String json = new String(data, StandardCharsets.UTF_8);
        return JSON.parseObject(json, cls);
    }
}
