package com.lbin.system.repository;

import com.lbin.jpa.repository.BaseRepository;
import com.lbin.system.domain.Permission;

/**
 * @author
 * @date 2018/8/14
 */
public interface PermissionRepository extends BaseRepository<Permission, Long> {

    /**
     * 根据标识查询角色数据,且排查指定ID的角色
     *
     * @param name 角色标识
     * @param id   角色ID
     * @return 角色信息
     */
    Boolean findByNameAndIdNot(String name, Long id);

}
