package com.lbin.system.service.impl;


import com.lbin.jpa.service.impl.BaseServiceImpl;
import com.lbin.system.domain.Role;
import com.lbin.system.repository.RoleRepository;
import com.lbin.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

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
