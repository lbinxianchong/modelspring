package com.lbin.server.system.repository;


import com.lbin.sql.jpa.repository.BaseRepository;
import com.lbin.server.system.domain.User;

/**
 * @author
 * @date 2018/8/14
 */
public interface UserRepository extends BaseRepository<User, Long> {

    /**
     * 根据用户名查询用户数据
     *
     * @param username 用户名
     * @return 用户数据
     */
    User findByUsername(String username);

    /**
     * 根据用户名查询用户数据,且排查指定ID的用户(名字查重复)
     *
     * @param username 用户名
     * @param id       排除的用户ID
     * @return 用户数据
     */
    Boolean existsByUsernameAndIdIsNot(String username, Long id);

}
