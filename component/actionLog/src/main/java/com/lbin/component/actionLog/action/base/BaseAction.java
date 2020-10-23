package com.lbin.component.actionLog.action.base;


import java.util.HashMap;

/**
 * @author
 * @date 2018/10/14
 */
public abstract class BaseAction {

    protected HashMap<String, Object> dictory = new HashMap<>();

    public BaseAction() {
        init();
    }

    /**
     * 初始化行为列表
     */
    public abstract void init();


}
