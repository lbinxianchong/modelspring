package com.lbin.server.system.service;


import com.lbin.sql.jpa.service.BaseService;
import com.lbin.server.system.domain.Role;

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
