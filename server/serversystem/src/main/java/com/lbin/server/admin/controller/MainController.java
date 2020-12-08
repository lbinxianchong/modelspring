package com.lbin.server.admin.controller;


import com.lbin.component.shiro.ShiroUtil;
import com.lbin.server.system.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * @author
 * @date 2018/8/14
 */
@Controller
public class MainController {

    /**
     * 后台主体内容
     */
    @GetMapping("/")
    public String main(Model model) {
        // 获取当前登录的用户
        User user = ShiroUtil.getSubject();

        model.addAttribute("user", user);
        return "/main";
    }

    /**
     * 主页
     */
    @GetMapping("/index")
    public String index(Model model) {
        return "/system/main/index";
    }

}
