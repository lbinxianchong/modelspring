package com.lbin.common.properties;

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
@ConfigurationProperties(prefix = "project.network")
public class NetworkProjectProperties {

    /**
     * cookie记住登录信息时间，默认14天
     */
    private Integer rememberMeTimeout = 14;

    /**
     * Session会话超时时间，默认60分钟
     */
    private Integer globalSessionTimeout = 3600;

    /**
     * Session会话检测间隔时间，默认15分钟
     */
    private Integer sessionValidationInterval = 900;

    /**
     * 默认忽略的路径规则，多个规则使用","逗号隔开
     */
    private String defaultExcludes = "/login,/logout,/captcha,/noAuth,/css/**,/js/**,/images/**,/lib/**,/favicon.ico";

    /**
     * 忽略的路径规则，多个规则使用","逗号隔开
     */
    private String excludes = "/api/**,/asset/**";


}
