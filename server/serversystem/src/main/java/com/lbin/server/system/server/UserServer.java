package com.lbin.server.system.server;

import com.lbin.common.constant.AdminConst;
import com.lbin.common.enums.ResultEnum;
import com.lbin.common.exception.ResultException;
import com.lbin.common.util.ResultVoUtil;
import com.lbin.common.vo.ResultVo;
import com.lbin.server.component.server.ComponentServer;
import com.lbin.server.system.domain.Role;
import com.lbin.server.system.domain.User;
import com.lbin.server.system.service.RoleService;
import com.lbin.server.system.service.UserService;
import com.lbin.server.system.validator.UserValid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Component
public class UserServer extends ComponentServer<User> {
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
    public ResultVo save(UserValid valid, User user) {
        // 不允许操作超级管理员数据
        if (user.getId().equals(AdminConst.ADMIN_ID)
//                && !ShiroUtil.getSubject().getId().equals(AdminConst.ADMIN_ID)
        ) {
            throw new ResultException(ResultEnum.NO_ADMIN_AUTH);
        }
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
    public Map<String,Object> toEditPassword(List<Long> ids) {
        Map<String,Object> model=new HashMap<>();
        model.put("idList", ids);
        return model;
    }

    /**
     * 修改密码
     */
    public ResultVo editPassword(String password, String confirm, List<Long> ids, List<User> users) {

        // 不允许操作超级管理员数据
        if (ids.contains(AdminConst.ADMIN_ID)
//                && !ShiroUtil.getSubject().getId().equals(AdminConst.ADMIN_ID)
        ) {
            throw new ResultException(ResultEnum.NO_ADMIN_AUTH);
        }

        // 判断密码是否为空
        if (password.isEmpty() || "".equals(password.trim())) {
            throw new ResultException(ResultEnum.USER_PWD_NULL);
        }

        // 判断两次密码是否一致
        if (!password.equals(confirm)) {
            throw new ResultException(ResultEnum.USER_INEQUALITY);
        }


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
    public Map<String,Object> toRole(User user) {
        Map<String,Object> model=new HashMap<>();
        // 获取指定用户角色列表
        Set<Role> authRoles = user.getRoles();
        // 获取全部角色列表
        List<Role> list = roleService.findAll();

        model.put("id", user.getId());
        model.put("list", list);
        model.put("authRoles", authRoles);
        return model;
    }

    /**
     * 保存角色分配信息
     */
    public ResultVo auth(User user, HashSet<Role> roles) {
        // 不允许操作超级管理员数据
        if (user.getId().equals(AdminConst.ADMIN_ID)
//                && !ShiroUtil.getSubject().getId().equals(AdminConst.ADMIN_ID)
        ) {
            throw new ResultException(ResultEnum.NO_ADMIN_AUTH);
        }

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
