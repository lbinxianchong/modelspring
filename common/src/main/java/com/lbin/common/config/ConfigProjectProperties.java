package com.lbin.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 项目-文件上传配置项
 *
 * @author
 * @date 2018/11/6
 */
@Data
@Component
@ConfigurationProperties(prefix = "project.config")
public class ConfigProjectProperties {

    /**
     * 是否读取config数据库配置
     */
    private boolean open = true;

    /**
     * 是否开启登录验证码
     */
    private boolean captchaOpen = false;

    /**
     * 后台名字
     */
    private String managename = "mo测试";




}
