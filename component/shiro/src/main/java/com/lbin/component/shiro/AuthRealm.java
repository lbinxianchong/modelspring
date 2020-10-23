//package com.lbin.component.shiro;
//
//import cn.hutool.core.bean.BeanUtil;
//import com.lbin.common.constant.AdminConst;
//import com.lbin.common.enums.StatusEnum;
//import com.lbin.common.utils.EntityBeanUtil;
//import com.lbin.component.shiro.domain.User;
//import com.lbin.modules.system.domain.Role;
//import com.lbin.modules.system.domain.User;
//import com.lbin.modules.system.service.UserService;
//import org.apache.shiro.authc.*;
//import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
//import org.apache.shiro.authz.AuthorizationInfo;
//import org.apache.shiro.authz.SimpleAuthorizationInfo;
//import org.apache.shiro.codec.CodecSupport;
//import org.apache.shiro.realm.AuthorizingRealm;
//import org.apache.shiro.subject.PrincipalCollection;
//import org.apache.shiro.util.ByteSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.util.StringUtils;
//
//import javax.annotation.PostConstruct;
//import java.util.Set;
//
///**
// * @author
// * @date 2018/8/14
// */
//public class AuthRealm extends AuthorizingRealm {
//
//    @Autowired
//    private UserService userService;
//
//    /**
//     * 授权逻辑
//     */
//    @Override
//    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
//        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        // 获取用户Principal对象
//        User user = (User) principal.getPrimaryPrincipal();
//
//        // 管理员拥有所有权限
//        if (user.getId().equals(AdminConst.ADMIN_ID)) {
//            info.addRole(AdminConst.ADMIN_ROLE_NAME);
//            info.addStringPermission("*:*:*");
//            return info;
//        }
//
//        // 赋予角色和资源授权
//        Set<Role> roles = ShiroUtil.getSubjectRoles();
//        roles.forEach(role -> {
//            info.addRole(role.getName());
//            role.getMenus().forEach(menu -> {
//                String perms = menu.getPerms();
//                if (menu.getStatus().equals(StatusEnum.OK.getCode())
//                        && !StringUtils.isEmpty(perms) && !perms.contains("*")) {
//                    info.addStringPermission(perms);
//                }
//            });
//        });
//
//        return info;
//    }
//
//    /**
//     * 认证逻辑
//     */
//    @Override
//    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
//        // 获取数据库中的用户名密码
//        User user = userService.getByName(token.getUsername());
//
//        // 判断用户名是否存在
//        if (user == null) {
//            throw new UnknownAccountException();
//        }
//
//        // 对盐进行加密处理
//        ByteSource salt = ByteSource.Util.bytes(user.getSalt());
//
//        // 克隆一个用户对象，隐藏用户密码及密码盐（目的不让密码在“记住我”状态下实例化到浏览器cookie中）
//        String[] ignores = {"password", "salt"};
//        Object principal = new Object();
//        BeanUtil.copyProperties(user, principal, ignores);
//        /* 传入密码自动判断是否正确
//         * 参数1：传入对象给Principal
//         * 参数2：正确的用户密码
//         * 参数3：加盐处理
//         * 参数4：固定写法
//         */
//        return new SimpleAuthenticationInfo(principal, user.getPassword(), salt, getName());
//    }
//
//    /**
//     * 自定义密码验证匹配器
//     */
//    @PostConstruct
//    public void initCredentialsMatcher() {
//        setCredentialsMatcher(new SimpleCredentialsMatcher() {
//            @Override
//            public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
//                UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
//                SimpleAuthenticationInfo info = (SimpleAuthenticationInfo) authenticationInfo;
//                // 获取明文密码及密码盐
//                String password = String.valueOf(token.getPassword());
//                String salt = CodecSupport.toString(info.getCredentialsSalt().getBytes());
//
//                return equals(ShiroUtil.encrypt(password, salt), info.getCredentials());
//            }
//        });
//    }
//}
