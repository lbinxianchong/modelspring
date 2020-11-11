package com.lbin.server.system.server;

import com.lbin.common.constant.AdminConst;
import com.lbin.common.enums.ResultEnum;
import com.lbin.common.exception.ResultException;
import com.lbin.common.util.ResultVoUtil;
import com.lbin.common.vo.ResultVo;
import com.lbin.server.component.server.ComponentServer;
import com.lbin.server.system.domain.Permission;
import com.lbin.server.system.domain.Role;
import com.lbin.server.system.service.PermissionService;
import com.lbin.server.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RoleServer extends ComponentServer<Role> {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Override
    public ResultVo save(Role role) {
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

    public Map<String, Object> toAuth(Long id) {
        Map<String, Object> model = new HashMap<>();
        model.put("id", id);
        return model;
    }

    /**
     * 获取权限资源列表
     */
    public ResultVo authList(Role role) {
        // 获取指定角色权限资源
        Set<Permission> authMenus = role.getPermissions();
        // 获取全部菜单列表
        List<Permission> list = permissionService.findAll();
        // 融合两项数据
        list.forEach(permission -> {
            if (authMenus.contains(permission)) {
                permission.setRemark("auth:true");
            } else {
                permission.setRemark("");
            }
        });
        return ResultVoUtil.success(list);
    }

    /**
     * 保存授权信息
     */
    public ResultVo auth(Role role, HashSet<Permission> permissions) {
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
    public Map<String,Object> toUserList(Role role) {
        Map<String,Object> model=new HashMap<>();
        model.put("list", role.getUsers());
        return model;
    }


    @Override
    public ResultVo status(String param, List<Long> ids) {
        if (ids.contains(AdminConst.ADMIN_ROLE_ID)) {
            throw new ResultException(ResultEnum.NO_ADMINROLE_STATUS);
        }
        return super.status(param, ids);
    }
}
