package com.lbin.server.system.service.impl;

import com.lbin.sql.jpa.service.impl.BaseServiceImpl;
import com.lbin.server.system.domain.Menu;
import com.lbin.server.system.repository.MenuRepository;
import com.lbin.server.system.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mo
 * @date 2020/12/11
 */
@Service
public class MenuServiceImpl extends BaseServiceImpl<Menu> implements MenuService {

    @Autowired
    private MenuRepository menuRepository;
}