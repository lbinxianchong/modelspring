package com.lbin.system.controller;


import com.lbin.common.vo.ResultVo;
import com.lbin.server.component.request.ComponentRequest;
import com.lbin.server.system.domain.Permission;
import com.lbin.server.system.domain.Role;
import com.lbin.server.system.server.RoleServer;
import com.lbin.system.validator.RoleValid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Map;


/**
 * @author
 * @date 2018/8/14
 */
@Controller
@RequestMapping("/system/role")
public class RoleController extends ComponentRequest<Role, RoleValid> {

    @Autowired
    private RoleServer roleServer;

    /**
     * 跳转到授权页面
     */
    @GetMapping("/auth")
    public Object toAuth(@RequestParam(value = "ids") Long id) {
        Map<String, Object> map = roleServer.toAuth(id);
        return getView(getThymeleafPath("/auth"), map);
    }

    /**
     * 获取权限资源列表
     */
    @GetMapping("/authList")
    @ResponseBody
    public ResultVo authList(@RequestParam(value = "ids") Role role) {
        return roleServer.authList(role);
    }

    /**
     * 保存授权信息
     */
    @PostMapping("/auth")
    @ResponseBody
    public ResultVo auth(
            @RequestParam(value = "id", required = true) Role role,
            @RequestParam(value = "authId", required = false) HashSet<Permission> permissions) {
        return roleServer.auth(role,permissions);
    }


    /**
     * 跳转到拥有该角色的用户列表页面
     */
    @GetMapping("/userList/{id}")
    public Object toUserList(@PathVariable("id") Role role) {
        Map<String, Object> map = roleServer.toUserList(role);
        return getView(getThymeleafPath("/user_list"), map);
    }

}
