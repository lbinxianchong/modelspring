package com.lbin.server.system.service.impl;


import com.lbin.sql.jpa.service.impl.BaseServiceImpl;
import com.lbin.server.system.domain.Role;
import com.lbin.server.system.repository.RoleRepository;
import com.lbin.server.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author
 * @date 2018/8/14
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {

    @Autowired
    private RoleRepository roleRepository;


    /**
     * 角色标识是否重复
     * @param role 角色实体类
     */
    @Override
    public Boolean repeatByName(Role role) {
        Long id = role.getId() != null ? role.getId() : Long.MIN_VALUE;
        return roleRepository.findByNameAndIdNot(role.getName(), id);
    }


}
