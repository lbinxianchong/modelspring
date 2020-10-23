package com.lbin.common.annotation;

import com.lbin.common.enums.BaseTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface BaseModel {

    // 字段类型
    BaseTypeEnum type() default BaseTypeEnum.Text;//BaseTypeEnum类型

    // 字段标题名称或文件名称
    String value() default "";

    // label
    String label() default "";

    // 字典
    String key() default "";

    //忽略字段
    boolean ignores() default false;

    boolean search() default false;

    boolean index() default false;

    boolean add() default true;

    boolean detail() default true;

}
