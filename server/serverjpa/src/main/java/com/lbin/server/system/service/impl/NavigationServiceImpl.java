package com.lbin.server.system.service.impl;

import com.lbin.sql.jpa.service.impl.BaseServiceImpl;
import com.lbin.server.system.domain.Navigation;
import com.lbin.server.system.repository.NavigationRepository;
import com.lbin.server.system.service.NavigationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mo
 * @date 2020/12/11
 */
@Service
public class NavigationServiceImpl extends BaseServiceImpl<Navigation> implements NavigationService {

    @Autowired
    private NavigationRepository navigationRepository;
}