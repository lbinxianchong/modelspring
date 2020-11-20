package com.lbin.server.system.controller;

import cn.hutool.core.bean.BeanUtil;
import com.lbin.common.util.CacheUtil;
import com.lbin.common.util.ResultVoUtil;
import com.lbin.common.vo.ResultVo;
import com.lbin.server.component.request.ComponentRequest;
import com.lbin.server.system.domain.Permission;
import com.lbin.server.system.server.PermissionServer;

import com.lbin.server.system.validator.PermissionValid;
import com.lbin.server.system.vo.Tree;
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
    public Object toAdd() {
        List<Permission> permissionList = getComponentServer().getBaseService().findAll();
        CacheUtil.saveCacheData("permissionlist",permissionList);
        return super.toAdd();
    }

    @GetMapping("/tree")
    @ResponseBody
    public ResultVo tree() {
        Map<String, Object> model = new HashMap<>();
        model.put("list", getTree(0L));
        return ResultVoUtil.success(model);
    }

    private List<Tree> getTree(Long pid) {
        Permission find = new Permission();
        find.setPid(pid);
        List<Permission> list = getComponentServer().getBaseService().findAll(find);
        if (list.size() == 0) {
            return null;
        }
        List<Tree> treeList = new ArrayList<>();
        for (Permission permission : list) {
            Tree tree = new Tree();
            BeanUtil.copyProperties(permission, tree);
            if (permission.getLevel() < 3) {
                tree.setChildren(getTree(permission.getId()));
            }
            treeList.add(tree);
        }
        return treeList;
    }
}
