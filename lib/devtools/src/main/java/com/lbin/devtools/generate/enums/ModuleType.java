package com.lbin.devtools.generate.enums;

import lombok.Getter;

/**
 * 代码生成-模块结构类型
 * @author
 * @date 2019/4/6
 */
@Getter
public enum ModuleType {
    /**
     * 会在业务组(modules)中生成一个新的模块(业务模块)，可以给前台模块使用
     */
    CUSTOM(1, "自定义模块"),
    /**
     * 将全部文件生成到后台模块(admin)中，复用性不强，如果只是开发后台管理项目，可直接使用这个结构
     */
    SYSTEM(2, "系统模块"),
    /**
     * 将全部文件生成到后台模块(admin)中，复用性不强，如果只是开发后台管理项目，可直接使用这个结构
     */
    MODEL(3, "模版模块");

    private Integer code;

    private String message;

    ModuleType(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
