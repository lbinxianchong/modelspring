package com.lbin.system.controller;

import cn.hutool.core.bean.BeanUtil;
import com.lbin.common.util.CacheUtil;
import com.lbin.common.util.ResultVoUtil;
import com.lbin.common.vo.ResultVo;
import com.lbin.server.component.request.ComponentRequest;
import com.lbin.server.system.domain.Permission;
import com.lbin.server.system.server.PermissionServer;

import com.lbin.system.validator.PermissionValid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author
 * @date 2018/8/14
 */
@Controller
@RequestMapping("/system/permission")
public class PermissionController extends ComponentRequest<Permission, PermissionValid> {

    @Autowired
    private PermissionServer permissionServer;

    @Override
    public Object toAdd(Permission permission) {
        List<Permission> permissionList = getComponentServer().getBaseService().findAll();
        CacheUtil.saveCacheData("permissionlist",permissionList);
        return super.toAdd(permission);
    }

}
