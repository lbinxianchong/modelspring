package com.lbin.system.service;


import com.lbin.jpa.service.BaseService;
import com.lbin.system.domain.Permission;

/**
 * @author
 * @date 2018/8/14
 */
public interface PermissionService extends BaseService<Permission> {

    /**
     * 权限标识是否重复
     * @param permission 权限
     * @return 标识是否重复
     */
    Boolean repeatByName(Permission permission);

}
