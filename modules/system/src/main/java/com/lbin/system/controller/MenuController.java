package com.lbin.system.controller;

import com.lbin.server.component.request.ComponentRequest;
import com.lbin.server.system.domain.Menu;
import com.lbin.server.system.server.MenuServer;
import com.lbin.system.validator.MenuValid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author mo
 * @date 2020/12/11
 */
@Controller
@RequestMapping("/system/menu")
public class MenuController extends ComponentRequest<Menu, MenuValid> {

    @Autowired
    private MenuServer menuServer;

}