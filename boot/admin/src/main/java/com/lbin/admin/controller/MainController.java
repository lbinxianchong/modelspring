package com.lbin.admin.controller;


import com.lbin.component.shiro.ShiroUtil;
import com.lbin.server.system.domain.User;
import com.lbin.server.system.domain.Navigation;
import com.lbin.server.system.service.NavigationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


/**
 * @author
 * @date 2018/8/14
 */
@Controller
public class MainController {

    @Autowired
    private NavigationService navigationService;

    /**
     * 后台主体内容
     */
    @GetMapping("/")
    public String main(Model model) {
        // 获取当前登录的用户
        User user = ShiroUtil.getSubject();
        // 获得导航栏
        List<Navigation> list = navigationService.findAll();

        model.addAttribute("user", user);
        model.addAttribute("list", list);

        return "/admin/main/main";
    }

    /**
     * 主页
     */
    @GetMapping("/index")
    public String index(Model model) {
        return "/admin/main/index";
    }

}
