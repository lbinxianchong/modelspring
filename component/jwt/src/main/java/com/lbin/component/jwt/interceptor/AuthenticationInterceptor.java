package com.lbin.component.jwt.interceptor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

import com.lbin.common.exception.ResultException;
import com.lbin.common.properties.NetworkProjectProperties;
import com.lbin.component.jwt.annotation.IgnorePermissions;
import com.lbin.component.jwt.annotation.JwtPermissions;

import com.lbin.component.jwt.enums.JwtResultEnums;
import com.lbin.component.jwt.utlis.JwtUtil;

import com.lbin.server.system.domain.Role;
import com.lbin.server.system.domain.User;
import com.lbin.server.system.service.UserService;
import com.lbin.sql.jpa.enums.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * jwt权限拦截器
 *
 * @author
 * @date 2019/4/12
 */
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private NetworkProjectProperties properties;

    @Autowired
    private UserService userService;

    /**
     * preHandle：在业务处理器处理请求之前被调用。预处理，可以进行编码、安全控制、权限校验等处理；
     * postHandle：在业务处理器处理请求执行完成后，生成视图之前执行。后处理（调用了Service并返回ModelAndView，但未进行页面渲染），有机会修改ModelAndView （这个博主就基本不怎么用了）；
     * afterCompletion：在DispatcherServlet完全处理完请求后被调用，可用于清理资源等。返回处理（已经渲染了页面）；
     */

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 判断请求映射的方式是否忽略权限验证
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(IgnorePermissions.class)) {
            return true;
        }

        // 获取请求对象头部token数据
        String token = JwtUtil.getRequestToken(request);

        // 验证token数据是否正确
        try {
            JwtUtil.verifyToken(token);
        } catch (TokenExpiredException e) {
            throw new ResultException(JwtResultEnums.TOKEN_EXPIRED);
        } catch (JWTVerificationException e) {
            throw new ResultException(JwtResultEnums.TOKEN_ERROR);
        }

        return isPrem(method);
//        return true;

    }

    private boolean isPrem(Method method) {
        boolean apiPrem = false;
        User user = userService.getByName(JwtUtil.getSubject());
        if (method.isAnnotationPresent(JwtPermissions.class)) {
            JwtPermissions jwtPermissions = method.getAnnotation(JwtPermissions.class);
            String prem = jwtPermissions.prem();
            if (prem == null || prem.trim().length() == 0) {
                apiPrem = true;
            } else {
                Set<Role> roles = user.getRoles();
                Set<String> premList = new HashSet<>();
                roles.forEach(role -> {
                    role.getPermissions().forEach(permission -> {
                        String perms = permission.getName();
                        if (permission.getStatus().equals(StatusEnum.OK.getCode()) && !StrUtil.isEmpty(perms) && !perms.contains("*")) {
                            premList.add(perms);
                        }
                    });
                });
                if (CollUtil.contains(premList, prem)) {
                    apiPrem = true;
                } else {
                    apiPrem = false;
                }
            }
        } else {
            apiPrem = true;
        }
        return apiPrem;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
