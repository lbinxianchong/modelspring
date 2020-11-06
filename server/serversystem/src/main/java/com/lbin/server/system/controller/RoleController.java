package com.lbin.server.system.controller;


import com.lbin.common.constant.AdminConst;
import com.lbin.common.enums.ResultEnum;
import com.lbin.common.exception.ResultException;
import com.lbin.common.util.ResultVoUtil;
import com.lbin.common.vo.ResultVo;
import com.lbin.server.component.controller.BaseController;
import com.lbin.server.system.domain.Permission;
import com.lbin.server.system.domain.Role;
import com.lbin.server.system.service.PermissionService;
import com.lbin.server.system.service.RoleService;
import com.lbin.server.system.validator.RoleValid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author
 * @date 2018/8/14
 */
@Controller
@RequestMapping("/system/role")
public class RoleController extends BaseController<Role, RoleValid> {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;


    @Override
    protected ResultVo save(Role role) {
        if (role.getId() != null && role.getId().equals(AdminConst.ADMIN_ROLE_ID)
//                && !ShiroUtil.getSubject().getId().equals(AdminConst.ADMIN_ID)
        ) {
            throw new ResultException(ResultEnum.NO_ADMINROLE_AUTH);
        }
        // 判断角色编号是否重复
        if (roleService.repeatByName(role)) {
            throw new ResultException(ResultEnum.ROLE_EXIST);
        }

        return super.save(role);
    }


    /**
     * 跳转到授权页面
     */
    @GetMapping("/auth")
    public String toAuth(@RequestParam(value = "ids") Long id, Model model) {
        model.addAttribute("id", id);
        return "/system/role/auth";
    }

    /**
     * 获取权限资源列表
     */
    @GetMapping("/authList")
    @ResponseBody
    public ResultVo authList(@RequestParam(value = "ids") Role role) {
        // 获取指定角色权限资源
        Set<Permission> authMenus = role.getPermissions();
        // 获取全部菜单列表
        List<Permission> list = permissionService.findAll();
        // 融合两项数据
        list.forEach(menu -> {
            if (authMenus.contains(menu)) {
                menu.setRemark("auth:true");
            } else {
                menu.setRemark("");
            }
        });
        return ResultVoUtil.success(list);
    }

    /**
     * 保存授权信息
     */
    @PostMapping("/auth")
    @ResponseBody
    public ResultVo auth(
            @RequestParam(value = "id", required = true) Role role,
            @RequestParam(value = "authId", required = false) HashSet<Permission> permissions) {
        // 不允许操作管理员角色数据
        if (role.getId().equals(AdminConst.ADMIN_ROLE_ID)
//                && !ShiroUtil.getSubject().getId().equals(AdminConst.ADMIN_ID)
        ) {
            throw new ResultException(ResultEnum.NO_ADMINROLE_AUTH);
        }

        // 更新角色菜单
        role.setPermissions(permissions);

        // 保存数据
        roleService.save(role);
        return ResultVoUtil.SAVE_SUCCESS;
    }


    /**
     * 跳转到拥有该角色的用户列表页面
     */
    @GetMapping("/userList/{id}")
    public String toUserList(@PathVariable("id") Role role, Model model) {
        model.addAttribute("list", role.getUsers());
        return "/system/role/user_list";
    }


    @Override
    public ResultVo status(String param, List<Long> ids) {
        if (ids.contains(AdminConst.ADMIN_ROLE_ID)) {
            throw new ResultException(ResultEnum.NO_ADMINROLE_STATUS);
        }
        return super.status(param, ids);
    }
}
