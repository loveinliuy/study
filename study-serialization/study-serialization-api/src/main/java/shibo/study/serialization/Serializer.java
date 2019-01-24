package shibo.study.serialization;

/**
 * 表示一个序列化工具
 *
 * @author zhangshibo
 */
public interface Serializer {
    /**
     * 序列化一个对象
     *
     * @param obj 对象
     * @param <T> 对象类型
     * @return 序列化得到的字节数组
     */
    <T> byte[] serialize(T obj);

    /**
     * 反序列化一个对象
     *
     * @param data 经过序列化得到的字节数组
     * @param cls  序列化时的对象
     * @param <T>  对象类型
     * @return 反序列化得到的结果
     */
    <T> T deserialize(byte[] data, Class<T> cls);
}
