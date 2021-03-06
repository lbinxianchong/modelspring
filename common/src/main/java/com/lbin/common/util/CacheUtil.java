package com.lbin.common.util;

import cn.hutool.core.bean.BeanUtil;
import com.lbin.cache.EhCacheUtil;
//import org.springframework.cache.Cache;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CacheUtil {

    public static Cache dictCache = EhCacheUtil.getDictCache();

    public static Cache configureCache = EhCacheUtil.getConfigureCache();

    public static Cache dataCache = EhCacheUtil.getDataCache();

    public static Cache getCache(String key) {
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

    public static Map<String, String> valueDict(String label) {
        return (Map<String, String>) value(dictCache, label);
    }

    public static String valueConfig(String label) {
        return (String) value(configureCache, label);
    }

    public static Object valueData(String label) {
        return value(dataCache, label);
    }


    /**
     * 根据选项编码获取选项值
     *
     * @param label 字典标识
     * @param code  选项编码
     */
    public static Object keyValue(Cache cache, String label, String code) {
        Object value = value(cache, label);
        if (value != null) {
            return BeanUtil.getFieldValue(value, code);
        } else {
            return null;
        }
    }

    /**
     * 根据选项编码获取选项值
     *
     * @param label 字典标识
     * @param code  选项编码
     */
    public static String keyValueDict(String label, String code) {
        Map<String, String> list = valueDict(label);
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
    public static void saveCacheDict(String label, Object value) {
        dictCache.put(new Element(label, value));
    }
    /**
     * 保存缓存
     *
     * @param label
     * @param value
     */
    public static void saveCacheConfig(String label, Object value) {
        configureCache.put(new Element(label, value));
    }
    /**
     * 保存缓存
     *
     * @param label
     * @param value
     */
    public static void saveCacheData(String label, Object value) {
        dataCache.put(new Element(label, value));
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
