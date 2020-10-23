package com.lbin.system.service.impl;


import com.lbin.jpa.service.impl.BaseServiceImpl;
import com.lbin.system.domain.Permission;
import com.lbin.system.repository.PermissionRepository;
import com.lbin.system.service.PermissionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author
 * @date 2018/8/14
 */
@Service
public class PermissionServiceImpl extends BaseServiceImpl<Permission> implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public Boolean repeatByName(Permission permission) {
        Long id = permission.getId() != null ? permission.getId() : Long.MIN_VALUE;
        return permissionRepository.findByNameAndIdNot(permission.getName(),id);
    }
}
