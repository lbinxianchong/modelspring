package com.lbin.server.system.controller;


import com.lbin.common.vo.ResultVo;
import com.lbin.server.component.request.ComponentRequest;
import com.lbin.server.system.domain.Role;
import com.lbin.server.system.domain.User;
import com.lbin.server.system.server.UserServer;
import com.lbin.server.system.validator.UserValid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author
 * @date 2018/8/14
 */
@Controller
@RequestMapping("/system/user")
public class UserController extends ComponentRequest<User, UserValid> {

    @Autowired
    private UserServer userServer;

    /**
     * 保存添加/修改的数据
     *
     * @param valid 验证对象
     * @param user  实体对象
     */

    @PostMapping("/save")
    @ResponseBody
    public ResultVo save(@Validated UserValid valid, User user) {
        return userServer.save(valid,user);
    }


    /**
     * 跳转到修改密码页面
     */
    @GetMapping("/pwd")
    public Object toEditPassword(@RequestParam(value = "ids", required = false) List<Long> ids) {
        Map<String, Object> map = userServer.toEditPassword(ids);
        return getView(getThymeleafPath("/pwd"),map);
    }

    /**
     * 修改密码
     */
    @PostMapping("/pwd")
    @ResponseBody
    public ResultVo editPassword(
            String password, String confirm,
            @RequestParam(value = "ids", required = false) List<Long> ids,
            @RequestParam(value = "ids", required = false) List<User> users) {
        return userServer.editPassword(password,confirm,ids,users);
    }

    /**
     * 跳转到角色分配页面
     */
    @GetMapping("/role")
    public Object toRole(@RequestParam(value = "ids") User user, Model model) {
        Map<String, Object> map = userServer.toRole(user);
        return getView(getThymeleafPath("/role"),map);
    }

    /**
     * 保存角色分配信息
     */
    @PostMapping("/role")
    @ResponseBody
    public ResultVo auth(
            @RequestParam(value = "id", required = true) User user,
            @RequestParam(value = "roleId", required = false) HashSet<Role> roles) {
        return userServer.auth(user,roles);
    }

}
