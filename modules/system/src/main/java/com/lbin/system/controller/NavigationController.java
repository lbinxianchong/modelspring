package com.lbin.system.controller;

import com.lbin.server.component.request.ComponentRequest;
import com.lbin.server.system.domain.Navigation;
import com.lbin.server.system.server.NavigationServer;
import com.lbin.system.validator.NavigationValid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author mo
 * @date 2020/12/11
 */
@Controller
@RequestMapping("/system/navigation")
public class NavigationController extends ComponentRequest<Navigation, NavigationValid> {

    @Autowired
    private NavigationServer navigationServer;

}