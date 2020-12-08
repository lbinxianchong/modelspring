package com.lbin.component.shiro.config;

import cn.hutool.core.util.StrUtil;
import com.lbin.server.system.domain.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.subject.Subject;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * 审核员自动赋值配置
 *
 * @author
 * @date 2018/8/14
 */
@Configuration
public class AuditorConfig implements AuditorAware<User> {
    @Override
    public Optional<User> getCurrentAuditor() {
        User user = null;
        try {
            Subject subject = SecurityUtils.getSubject();
            user = (User) subject.getPrincipal();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(user);
    }
}
