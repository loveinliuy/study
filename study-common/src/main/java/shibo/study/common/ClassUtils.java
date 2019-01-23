package shibo.study.common;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangshibo
 */
public final class ClassUtils {

    private ClassUtils() {
    }

    /**
     * 获取一个类实现的的所有接口
     *
     * @param targetClass 需要获取的类
     * @return 这个类实现的所有接口
     */
    public static List<Class<?>> getAllInterfaces(Class<?> targetClass) {
        List<Class<?>> allInterfaces = Arrays.stream(targetClass.getInterfaces()).collect(Collectors.toList());
        if (targetClass.isInterface()) {
            allInterfaces.add(targetClass);
        }
        return allInterfaces;
    }

}
