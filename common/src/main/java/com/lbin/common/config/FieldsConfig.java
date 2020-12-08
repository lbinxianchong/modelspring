package com.lbin.common.config;

public class FieldsConfig {

    /**
     * 忽略的默认字段
     */
    public static String[] ignoresFields = new String[]{};
    /**
     * 忽略的默认字段
     */
    public static String[] searchFields = new String[]{};
    /**
     * 忽略的默认字段
     */
    public static String[] indexFields = new String[]{};
    /**
     * 忽略的默认字段
     */
    public static String[] addFields = new String[]{
            "id",
            "createDate",
            "updateDate",
            "createBy",
            "updateBy",
            "status"
    };
    /**
     * 忽略的默认字段
     */
    public static String[] detailFields = new String[]{};

    /**
     * 复制实体对象保留的默认字段
     */
    public static String[] defaultFields = new String[]{
            "createDate",
            "updateDate",
            "createBy",
            "updateBy",
            "status"
    };
}
