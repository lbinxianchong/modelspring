package com.lbin.component.shiro;

import cn.hutool.core.util.RandomUtil;

import com.lbin.common.util.EncryptUtil;
import com.lbin.common.util.SpringContextUtil;
import com.lbin.server.system.domain.Role;
import com.lbin.server.system.domain.User;
import com.lbin.server.system.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.hibernate.Hibernate;
import org.hibernate.LazyInitializationException;

import java.util.Set;

/**
 * Shiro工具类
 *
 * @author
 * @date 2018/8/14
 */
public class ShiroUtil {

    /**
     * 加密处理
     * 备注：采用自定义的密码加密方式，其原理与SimpleHash一致，
     * 为的是在多个模块间可以使用同一套加密方式，方便共用系统用户。
     *
     * @param password 密码
     * @param salt     密码盐
     */
    public static String encrypt(String password, String salt) {
        return EncryptUtil.encrypt(password, salt);
    }

    /**
     * 获取随机盐值
     */
    public static String getRandomSalt() {
        return RandomUtil.randomString(12);
    }

    /**
     * 获取当前用户对象
     */
    public static User getSubject() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        return user;
    }

    /**
     * 获取当前用户角色列表
     */
    static Set<Role> getSubjectRoles() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();

        // 如果用户为空，则返回空列表
        if (user == null) {
            user = new User();
        }

        // 判断角色列表是否已缓存
        if (!Hibernate.isInitialized(user.getRoles())) {
            try {
                Hibernate.initialize(user.getRoles());
            } catch (LazyInitializationException e) {
                // 延迟加载超时，重新查询角色列表数据
                UserService userService = SpringContextUtil.getBean(UserService.class);
                user.setRoles(userService.getById(user.getId()).getRoles());
            }
        }

        return user.getRoles();
    }

    /**
     * 获取用户IP地址
     */
    public static String getIp() {
        return SecurityUtils.getSubject().getSession().getHost();
    }

    public static boolean hasRole(String role){
        return SecurityUtils.getSubject().hasRole(role);
    }

    public static boolean isPermitted(String perm){
        return SecurityUtils.getSubject().isPermitted(perm);
    }
}
