package com.lbin.component.thymeleaf.utility;


import com.lbin.common.util.CacheUtil;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import java.util.Map;

/**
 * 字典提取工具对象
 * @author
 * @date 2018/8/14
 */
public class DictUtil {

    private static Cache dictCache = CacheUtil.dictCache;

    private static Cache configureCache = CacheUtil.configureCache;



    /**
     * 获取字典值集合
     * @param label 字典标识
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> value(String label){
        Map<String, String> value = null;
        Element dictEle = dictCache.get(label);
        if(dictEle != null){
            value = (Map<String, String>) dictEle.getObjectValue();
        }
        return value;
    }

    /**
     * 根据选项编码获取选项值
     * @param label 字典标识
     * @param code 选项编码
     */
    public static String keyValue(String label, String code){
        Map<String, String> list = DictUtil.value(label);
        if(list != null){
            return list.get(code);
        }else{
            return "";
        }
    }

    /**
     * 封装数据状态字典
     * @param status 状态
     */
    public static String dataStatus(Byte status){
        String label = "DATA_STATUS";
        return DictUtil.keyValue(label, String.valueOf(status));
    }

    /**
     * 清除缓存中指定的数据
     * @param label 字典标识
     */
    public static void clearCache(String label){
        Element dictEle = dictCache.get(label);
        if (dictEle != null){
            dictCache.remove(label);
        }
    }


    /**
     * 根据选项编码获取选项值
     * @param label 字典标识
     */
    @SuppressWarnings("unchecked")
    public static String keyValueConfig(String label){
        String value = "";
        Element dictEle = configureCache.get(label);
        if(dictEle != null){
            value = (String) dictEle.getObjectValue();
        }
        return value;
    }
}
