package com.lbin.server.system.service;

import com.lbin.sql.jpa.service.BaseService;
import com.lbin.server.system.domain.User;

/**
 * @author
 * @date 2018/8/14
 */
public interface UserService extends BaseService<User> {

    /**
     * 根据用户名查询用户数据
     * @param username 用户名
     * @return 用户数据
     */
    User getByName(String username);

    /**
     * 用户名是否重复
     * @param user 用户对象
     * @return 用户数据
     */
    Boolean repeatByUsername(User user);

}
