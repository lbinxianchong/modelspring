package com.lbin.cache;


import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * EhCache缓存操作工具
 *
 * @author
 * @date 2018/11/7
 */
public class EhCacheUtil {

//    @Autowired
    private static CacheManager cacheManager;

    /**
     * 获取EhCacheManager管理对象
     */
    public static CacheManager getCacheManager() {
        if (cacheManager == null) {
            cacheManager = CacheManager.getInstance();
        }
        return cacheManager;
    }

    /**
     * 获取EhCache缓存对象
     */
    public static Cache getCache(String name) {
        return getCacheManager().getCache(name);
    }

    /**
     * 获取字典缓存对象
     */
    public static Cache getDictCache() {
        return getCache("dictionary");
    }

    /**
     * 获取配置缓存对象
     */
    public static Cache getConfigureCache() {
        return getCache("configure");
    }

    /**
     * 获取通用数据缓存对象
     */
    public static Cache getDataCache() {
        return getCache("data");
    }



}
