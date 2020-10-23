package com.lbin.system.service;


import com.lbin.jpa.service.BaseService;
import com.lbin.system.domain.Role;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @author
 * @date 2018/8/14
 */
public interface RoleService extends BaseService<Role> {

    /**
     * 角色标识是否重复
     * @param role 角色实体类
     * @return 标识是否重复
     */
    Boolean repeatByName(Role role);

}
