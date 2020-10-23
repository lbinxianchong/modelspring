package com.lbin.system.repository;

import com.lbin.jpa.repository.BaseRepository;
import com.lbin.system.domain.Role;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @author
 * @date 2018/8/14
 */
public interface RoleRepository extends BaseRepository<Role, Long> {

    /**
     * 根据标识查询角色数据,且排查指定ID的角色
     *
     * @param name 角色标识
     * @param id   角色ID
     * @return 角色信息
     */
    Boolean findByNameAndIdNot(String name, Long id);

    /**
     * 判断指定的用户是否存在角色
     *
     * @param id     用户ID
     * @param status 角色状态
     * @return 是否存在角色
     */
    Boolean existsByUsers_IdAndStatus(Long id, Byte status);
}
