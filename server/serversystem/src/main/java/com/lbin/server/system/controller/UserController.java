package com.lbin.server.system.controller;


import com.lbin.common.constant.AdminConst;
import com.lbin.common.enums.ResultEnum;

import com.lbin.common.exception.ResultException;
import com.lbin.common.util.ResultVoUtil;
import com.lbin.common.vo.ResultVo;
import com.lbin.server.component.controller.BaseController;
import com.lbin.server.system.domain.Role;
import com.lbin.server.system.domain.User;
import com.lbin.server.system.service.RoleService;
import com.lbin.server.system.service.UserService;
import com.lbin.server.system.validator.UserValid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author
 * @date 2018/8/14
 */
@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController<User, UserValid> {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;


    /**
     * 保存添加/修改的数据
     *
     * @param valid 验证对象
     * @param user  实体对象
     */

    @PostMapping("/save")
    @ResponseBody
    public ResultVo save(@Validated UserValid valid, User user) {
        authAdmin(user.getId());
        // 验证数据是否合格
        if (user.getId() == null) {

            // 判断密码是否为空
            if (user.getPassword().isEmpty() || "".equals(user.getPassword().trim())) {
                throw new ResultException(ResultEnum.USER_PWD_NULL);
            }

            // 判断两次密码是否一致
            if (!user.getPassword().equals(valid.getConfirm())) {
                throw new ResultException(ResultEnum.USER_INEQUALITY);
            }

            encrypt(user);

        }

        // 判断用户名是否重复
        if (userService.repeatByUsername(user)) {
            throw new ResultException(ResultEnum.USER_EXIST);
        }

        return save(user);
    }


    /**
     * 跳转到修改密码页面
     */
    @GetMapping("/pwd")
    public String toEditPassword(Model model, @RequestParam(value = "ids", required = false) List<Long> ids) {
        model.addAttribute("idList", ids);
        return "/system/user/pwd";
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

        // 判断密码是否为空
        if (password.isEmpty() || "".equals(password.trim())) {
            throw new ResultException(ResultEnum.USER_PWD_NULL);
        }

        // 判断两次密码是否一致
        if (!password.equals(confirm)) {
            throw new ResultException(ResultEnum.USER_INEQUALITY);
        }

        authAdmin(ids);

        // 修改密码，对密码进行加密
        users.forEach(user -> {
            encrypt(user);
        });

        // 保存数据
        userService.saveAll(users);
        return ResultVoUtil.success("修改成功");
    }

    /**
     * 跳转到角色分配页面
     */
    @GetMapping("/role")
    public String toRole(@RequestParam(value = "ids") User user, Model model) {
        // 获取指定用户角色列表
        Set<Role> authRoles = user.getRoles();
        // 获取全部角色列表
        List<Role> list = roleService.findAll();

        model.addAttribute("id", user.getId());
        model.addAttribute("list", list);
        model.addAttribute("authRoles", authRoles);
        return "/system/user/role";
    }

    /**
     * 保存角色分配信息
     */
    @PostMapping("/role")
    @ResponseBody
    public ResultVo auth(
            @RequestParam(value = "id", required = true) User user,
            @RequestParam(value = "roleId", required = false) HashSet<Role> roles) {
        authAdmin(user.getId());

        // 更新用户角色
        user.setRoles(roles);

        // 保存数据
        userService.save(user);
        return ResultVoUtil.SAVE_SUCCESS;
    }


    @Override
    public ResultVo status(String param, List<Long> ids) {
        if (ids.contains(AdminConst.ADMIN_ID)) {
            throw new ResultException(ResultEnum.NO_ADMIN_STATUS);
        }
        return super.status(param, ids);
    }


    /**
     * 不允许操作超级管理员数据
     *
     * @param id
     * @return
     */
    private void authAdmin(Long id) {
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        authAdmin(ids);
    }

    /**
     * 不允许操作超级管理员数据
     *
     * @param ids
     * @return
     */
    private void authAdmin(List<Long> ids) {
        // 不能修改超级管理员状态
        boolean equals = ids.contains(AdminConst.ADMIN_ID);
//        boolean admin= !ShiroUtil.getSubject().getId().equals(AdminConst.ADMIN_ID);
        boolean admin = true;
        if (equals && admin) {
            throw new ResultException(ResultEnum.NO_ADMIN_AUTH);
        }
    }

    /**
     * 加密密文
     *
     * @param user
     * @return
     */
    private User encrypt(User user) {
        // 对密码进行加密
//        String salt = ShiroUtil.getRandomSalt();
//        String encrypt = ShiroUtil.encrypt(user.getPassword(), salt);
//        user.setPassword(encrypt);
//        user.setSalt(salt);
        return user;
    }

}
