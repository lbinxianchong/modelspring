package com.lbin.common.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface BaseIgnoresModel {
    // 字段标题名称或文件名称
    String value() default "";

    String[] ignores() default {};

    String[] search() default {};

    String[] index() default {};

    String[] add() default {"id", "createDate", "updateDate", "createBy", "updateBy", "status"};

    String[] detail() default {};
}
