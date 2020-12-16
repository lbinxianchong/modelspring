package com.lbin.admin.controller;

import com.lbin.common.enums.ResultEnum;
import com.lbin.common.exception.ResultException;
import com.lbin.common.util.ResultVoUtil;
import com.lbin.common.vo.ResultVo;
import com.lbin.component.shiro.ShiroUtil;
import com.lbin.server.system.domain.User;
import com.lbin.server.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 验证顺序
 * RequiresRoles
 * RequiresPermissions
 * RequiresAuthentication
 * RequiresUser
 * RequiresGuest
 */
//@RequiresRoles("")
//当前Subject必须拥有所有指定的角色时，才能访问被该注解标注的方法。如果当天Subject不同时拥有所有指定角色，则方法不会执行还会抛出AuthorizationException异常。
//@RequiresPermissions("")
//当前Subject需要拥有某些特定的权限时，才能执行被该注解标注的方法。如果当前Subject不具有这样的权限，则方法不会被执行。
//@RequiresUser
//当前Subject必须是应用的用户，才能访问或调用被该注解标注的类，实例，方法。
//@RequiresAuthentication
//使用该注解标注的类，实例，方法在访问或调用时，当前Subject必须在当前session中已经过认证。
//@RequiresGuest
//使用该注解标注的类，实例，方法在访问或调用时，当前Subject可以是“gust”身份，不需要经过认证或者在原先的session中存在记录。
@Controller
public class OptionController {

    @Autowired
    private UserService userService;

    /**
     * 跳转到个人信息页面
     */
    @GetMapping("/userInfo")
    public String toUserInfo(Model model) {
        User user = ShiroUtil.getSubject();
        model.addAttribute("model", user);
        return "/system/main/userInfo";
    }

    /**
     * 跳转到修改密码页面
     */
    @GetMapping("/editPwd")
    public String toEditPwd() {
        return "/system/main/editPwd";
    }

    /**
     * 保存修改密码
     */
    @PostMapping("/editPwd")
    @ResponseBody
    public ResultVo editPwd(String original, String password, String confirm) {
        // 判断原来密码是否有误
        User subUser = ShiroUtil.getSubject();
        String oldPwd = ShiroUtil.encrypt(original, subUser.getSalt());
        if (original.isEmpty() || "".equals(original.trim()) || !oldPwd.equals(subUser.getPassword())) {
            throw new ResultException(ResultEnum.USER_OLD_PWD_ERROR);
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
        String salt = ShiroUtil.getRandomSalt();
        String encrypt = ShiroUtil.encrypt(password, salt);
        subUser.setPassword(encrypt);
        subUser.setSalt(salt);

        // 保存数据
        userService.save(subUser);
        return ResultVoUtil.success("修改成功");
    }
}
