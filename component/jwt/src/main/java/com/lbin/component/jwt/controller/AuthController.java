package com.lbin.component.jwt.controller;


import com.lbin.common.config.EncryptConfig;
import com.lbin.common.exception.ResultException;
import com.lbin.common.properties.NetworkProjectProperties;
import com.lbin.common.util.EncryptUtil;

import com.lbin.common.util.ResultVoUtil;
import com.lbin.common.vo.ResultVo;
import com.lbin.component.jwt.annotation.IgnorePermissions;
import com.lbin.component.jwt.enums.JwtResultEnums;
import com.lbin.component.jwt.utlis.JwtUtil;

import com.lbin.server.system.domain.User;
import com.lbin.server.system.service.UserService;
import com.lbin.sql.jpa.enums.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 默认登录验证控制器
 * 说明：默认采用系统用户进行登录验证
 * @author
 * @date 2019/4/9
 */
@RestController
public class AuthController {

    @Autowired
    private NetworkProjectProperties properties;

    @Autowired
    private UserService userService;

    @IgnorePermissions
    @PostMapping("/api/auth")
    public ResultVo auth(String username, String password){
        // 根据用户名获取系统用户数据
        User user = userService.getByName(username);
        if (user == null) {
            throw new ResultException(JwtResultEnums.AUTH_REQUEST_ERROR);
        } else if (user.getStatus().equals(StatusEnum.FREEZED.getCode())){
            throw new ResultException(JwtResultEnums.AUTH_REQUEST_LOCKED);
        } else {
            // 对明文密码加密处理
            String encrypt = EncryptUtil.encrypt(password, user.getSalt());
            // 判断密码是否正确
            if (encrypt.equals(user.getPassword())) {
                String token = JwtUtil.getToken(username, EncryptConfig.secret, properties.getRememberMeTimeout());
                return ResultVoUtil.success("登录成功", token);
            } else {
                throw new ResultException(JwtResultEnums.AUTH_REQUEST_ERROR);
            }
        }
    }
}
