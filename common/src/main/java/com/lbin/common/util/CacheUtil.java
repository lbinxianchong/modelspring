package com.lbin.common.util;

import com.lbin.cache.EhCacheUtil;
//import org.springframework.cache.Cache;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CacheUtil {

    private static Cache dictCache = EhCacheUtil.getDictCache();

    private static Cache configureCache = EhCacheUtil.getConfigureCache();

    private static Cache dataCache = EhCacheUtil.getDataCache();

    public static Cache getCache(String key){
        return EhCacheUtil.getCache(key);
    }

    @SuppressWarnings("unchecked")
    public static Object value(Cache cache, String label) {
        Object value = null;
        Element dictEle = cache.get(label);
        if (dictEle != null) {
            value = dictEle.getObjectValue();
        }
        return value;
    }

    public static Map<String, String> valueMap(Cache cache, String label) {
        return (Map<String, String>) value(cache, label);
    }

    /**
     * 根据选项编码获取选项值
     *
     * @param label 字典标识
     * @param code  选项编码
     */
    public static String keyValue(Cache cache, String label, String code) {
        Map<String, String> list = valueMap(cache, label);
        if (list != null) {
            return list.get(code);
        } else {
            return "";
        }
    }

    /**
     * 保存缓存
     *
     * @param label
     * @param value
     */
    public static void saveCache(Cache cache, String label, Object value) {
        cache.put(new Element(label, value));
    }

    /**
     * 保存缓存
     *
     * @param label
     * @param value
     */
    public static void saveCache(Cache cache, String label, List<Object> list, String key, String value) {
        Map<Object, Object> map = new LinkedHashMap<>();
        for (Object o : list) {
            Object fieldkey = ReflectUtil.getFieldValue(o, key);
            Object fieldvalue = ReflectUtil.getFieldValue(o, value);
            map.put(fieldkey, fieldvalue);
        }
        saveCache(cache, label, map);
    }

    /**
     * 清除缓存中指定的数据
     *
     * @param label 字典标识
     */
    public static void clearCache(Cache cache, String label) {
        Element dictEle = cache.get(label);
        if (dictEle != null) {
            cache.remove(label);
        }
    }

    /**
     * 清除缓存
     */
    public static void clearCache(Cache cache) {
        cache.removeAll();
    }
}
