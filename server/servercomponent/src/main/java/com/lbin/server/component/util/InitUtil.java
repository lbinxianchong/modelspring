package com.lbin.server.component.util;


import com.lbin.common.domain.BaseFieldModel;
import com.lbin.common.util.ReflectUtil;
import com.lbin.server.component.server.ComponentServer;
import com.lbin.sql.jpa.annotation.AutowiredBaseModel;
import com.lbin.sql.jpa.service.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

/**
 * 初始化工具
 */
public class InitUtil {

    public static <T> BaseService<T> getBaseService(Object object) {
        return getFieldValue(object, BaseService.class, "baseService");
    }

    public static <T> ComponentServer<T> getComponentServer(Object object) {
        return getFieldValue(object, ComponentServer.class, "componentServer");
    }

    public static <T> T getFieldValue(Object object, Class<T> tClass, String s) {
        T t = (T) ReflectUtil.getFieldValue(object, s);
        if (t == null) {
            try {
                for (Field declaredField : object.getClass().getDeclaredFields()) {
                    declaredField.setAccessible(true);
                    Object o = declaredField.get(object);
                    if (o != null &&
                            tClass.isAssignableFrom(o.getClass()) &&
                            !declaredField.getName().equals(s)) {
                        t = (T) o;
                        if (declaredField.isAnnotationPresent(AutowiredBaseModel.class)) {
                            break;
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return t;
    }

    public static <T> Class<T> getTClass(Class<?> t) {
        return (Class<T>) ((ParameterizedType) t.getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public static String getRequestMapping(String requestMapping, Class<?> c) {
        RequestMapping annotation = c.getAnnotation(RequestMapping.class);
        if (annotation != null) {
            requestMapping = annotation.value()[0];
        }
        return requestMapping;
    }

    public static BaseFieldModel getBaseFieldModel(Class<?> c) {
        BaseFieldModel baseFieldModel = new BaseFieldModel(InitUtil.getTClass(c));
        return baseFieldModel;
    }


}
