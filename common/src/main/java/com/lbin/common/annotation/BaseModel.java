package com.lbin.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface BaseModel {

    // 字段标题名称或文件名称
    String value() default "";

    // label
    String label() default "";

    // 字典
    String key() default "";

    boolean search() default false;

    boolean index() default false;

    boolean add() default true;

    boolean detail() default true;
}
