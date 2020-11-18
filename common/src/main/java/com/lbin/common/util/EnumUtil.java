package com.lbin.common.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class EnumUtil {


    /**
     * 将枚举转成Map集合
     *
     * @param enumClass 枚举类
     */
    public static Map<Integer, String> enumToMap(Class<?> enumClass) {
        Map<Integer, String> map = enumToMap(enumClass, "code", "message");
        return map;
    }

    /**
     * 将枚举转成Map集合
     *
     * @param enumClass 枚举类
     */
    public static <T, S> Map<T, S> enumToMap(Class<?> enumClass, String key, String value) {
        Map<T, S> map = new TreeMap<>();
        List<Map<String, Object>> list = enumToListMap(enumClass, key, value);
        for (Map<String, Object> objectMap : list) {
            T keyObject = (T) objectMap.get(key);
            S valueObject = (S) objectMap.get(value);
            map.put(keyObject, valueObject);
        }
        return map;
    }

    /**
     * 将枚举转成List集合
     *
     * @param enumClass 枚举类
     */
    public static List<Map<String, Object>> enumToListMap(Class<?> enumClass, String... keys) {
        List<Map<String, Object>> list = new ArrayList<>();
        Object[] objects = enumClass.getEnumConstants();
        for (Object obj : objects) {
            Map<String, Object> map = new HashMap<>();
            for (String key : keys) {
                Object value = ReflectUtil.getFieldValue(obj, key);
                map.put(key, value);
            }
            list.add(map);
        }
        return list;
    }

    /**
     * 根据枚举code获取枚举对象
     *
     * @param enumClass 枚举类
     * @param code      code值
     */
    public static Object enumCode(Class<?> enumClass, Object code) {
        Object[] objects = enumClass.getEnumConstants();
        for (Object obj : objects) {
            Object value = ReflectUtil.getFieldValue(obj, "code");
            if (value.equals(code)) {
                return obj;
            }
        }
        return "";
    }
}
