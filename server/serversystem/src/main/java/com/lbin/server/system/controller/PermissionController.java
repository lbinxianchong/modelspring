package com.lbin.server.system.controller;


import com.lbin.server.component.controller.BaseController;

import com.lbin.server.system.domain.Permission;
import com.lbin.server.system.service.PermissionService;
import com.lbin.server.system.validator.PermissionValid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author
 * @date 2018/8/14
 */
@Controller
@RequestMapping("/system/permission")
public class PermissionController extends BaseController<Permission, PermissionValid> {

    @Autowired
    private PermissionService permissionService;

}
