//package com.lbin.component.shiro;
//
//import cn.hutool.core.util.StrUtil;
//import com.lbin.common.utils.EncryptUtil;
//import com.lbin.common.utils.HttpServletUtil;
//import com.lbin.common.utils.SpringContextUtil;
//import com.lbin.modules.system.domain.Role;
//import com.lbin.modules.system.domain.User;
//import com.lbin.modules.system.service.RoleService;
//import com.lbin.modules.system.service.UserService;
//import org.apache.shiro.SecurityUtils;
//import org.hibernate.Hibernate;
//import org.hibernate.LazyInitializationException;
//import org.springframework.beans.BeanUtils;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Set;
//
///**
// * Shiro工具类
// *
// * @author
// * @date 2018/8/14
// */
//public class ShiroUtil {
//
//    /**
//     * 加密算法
//     */
//    public final static String HASH_ALGORITHM_NAME = EncryptUtil.HASH_ALGORITHM_NAME;
//
//    /**
//     * 循环次数
//     */
//    public final static int HASH_ITERATIONS = EncryptUtil.HASH_ITERATIONS;
//
//    /**
//     * 加密处理
//     * 备注：采用自定义的密码加密方式，其原理与SimpleHash一致，
//     * 为的是在多个模块间可以使用同一套加密方式，方便共用系统用户。
//     *
//     * @param password 密码
//     * @param salt     密码盐
//     */
//    public static String encrypt(String password, String salt) {
//        return EncryptUtil.encrypt(password, salt, HASH_ALGORITHM_NAME, HASH_ITERATIONS);
//    }
//
//    /**
//     * 获取随机盐值
//     */
//    public static String getRandomSalt() {
//        return EncryptUtil.getRandomSalt();
//    }
//
//    /**
//     * 获取当前用户对象
//     */
//    public static User getSubject() {
//        User user = (User) SecurityUtils.getSubject().getPrincipal();
//
//        // 初始化延迟加载的部门信息
//        if (user != null && !Hibernate.isInitialized(user.getDept())) {
//            try {
//                Hibernate.initialize(user.getDept());
//            } catch (LazyInitializationException e) {
//                // 部门数据延迟加载超时，重新查询用户数据（用于更新“记住我”状态登录的数据）
//                UserService userService = SpringContextUtil.getBean(UserService.class);
//                User reload = userService.getById(user.getId());
//                Hibernate.initialize(reload.getDept());
//                // 将重载用户数据拷贝到登录用户中，忽略掉密码和密码盐
//                BeanUtils.copyProperties(reload, user, "password", "salt", "roles");
//            }
//        }
//
//        return user;
//    }
//
//    /**
//     * 获取当前用户角色列表
//     */
//    static Set<Role> getSubjectRoles() {
//        User user = (User) SecurityUtils.getSubject().getPrincipal();
//
//        // 如果用户为空，则返回空列表
//        if (user == null) {
//            user = new User();
//        }
//
//        // 判断角色列表是否已缓存
//        if (!Hibernate.isInitialized(user.getRoles())) {
//            try {
//                Hibernate.initialize(user.getRoles());
//            } catch (LazyInitializationException e) {
//                // 延迟加载超时，重新查询角色列表数据
//                RoleService roleService = SpringContextUtil.getBean(RoleService.class);
//                user.setRoles(roleService.getUserOkRoleList(user.getId()));
//            }
//        }
//
//        return user.getRoles();
//    }
//
//    /**
//     * 获取用户IP地址
//     */
//    public static String getIp() {
//        return SecurityUtils.getSubject().getSession().getHost();
//    }
//
//    public static boolean hasRole(String role){
//        return SecurityUtils.getSubject().hasRole(role);
//    }
//    public static boolean isPermitted(String perm){
//        return SecurityUtils.getSubject().isPermitted(perm);
//    }
//
//
//}
