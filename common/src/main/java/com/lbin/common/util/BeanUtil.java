package com.lbin.common.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

public class BeanUtil {
    /**
     * 获得字段值，通过反射直接获得字段值，并不调用getXXX方法<br>
     * 对象同样支持Map类型，fieldNameOrIndex即为key
     *
     * @param bean             Bean对象
     * @param fieldNameOrIndex 字段名或序号，序号支持负数
     * @return 字段值
     */
    public static Object getFieldValue(Object bean, String fieldNameOrIndex) {
        if (null == bean || null == fieldNameOrIndex) {
            return null;
        }
        if (bean instanceof Map) {
            return ((Map<?, ?>) bean).get(fieldNameOrIndex);
        } else if (bean instanceof Collection) {
            int i = Integer.parseInt(fieldNameOrIndex);
            Object ob = null;
            for (Object o : (Collection<?>) bean) {
                if (i == 0) {
                    ob = o;
                    break;
                }
                i--;
            }
            return ob;
        } else if (bean.getClass().isArray()) {
            return Array.get(bean, Integer.parseInt(fieldNameOrIndex));
        } else {// 普通Bean对象
            return ReflectUtil.getFieldValue(bean, fieldNameOrIndex);
        }
    }
}
