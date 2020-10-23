package com.lbin.component.actionLog.domain;

import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

/**
 * @author
 * @date 2018/10/19
 */
@Data
public class BaseLog implements Serializable {
    //日志名称
    protected String name;
    //日志类型
    protected Byte type;
    //日志级别
    protected Integer level;

    /**
     * 操作记录
     */

    //记录时间
    protected Date createDate;
    //操作人uuid
    protected String operuuid;
    //操作人名称
    protected String operName;
    //密文
    protected String code;
    //行为记录
    protected String run;

    /**
     * 操作参数
     */

    //操作IP地址
    protected String ipaddr;
    //数据模型(类别，例：请求、调度、操作数据库)
    protected String model;
    //操作url地址
    protected String url;
    //操作类
    protected Class entity;
    //操作方法
    protected Method method;
    //操作参数
    protected List<Object> params;

    /**
     * 操作结果
     */

    //异常状态
    protected String exception;
    //日志消息
    protected String message;


}
