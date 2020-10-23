package com.lbin.system.service.impl;


import com.lbin.jpa.service.impl.BaseServiceImpl;
import com.lbin.system.domain.User;
import com.lbin.system.repository.UserRepository;
import com.lbin.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @date 2018/8/14
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 根据用户名查询用户数据
     * @param username 用户名
     * @return 用户数据
     */
    @Override
    public User getByName(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * 用户名是否存在
     * @param user 用户对象
     * @return 用户数据
     */
    @Override
    public Boolean repeatByUsername(User user) {
        Long id = user.getId() != null ? user.getId() : Long.MIN_VALUE;
        return userRepository.existsByUsernameAndIdIsNot(user.getUsername(), id);
    }

}
