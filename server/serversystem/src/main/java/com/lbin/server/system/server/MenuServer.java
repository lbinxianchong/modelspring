package com.lbin.server.system.server;

import com.lbin.server.component.server.ComponentServer;
import com.lbin.server.system.domain.Menu;
import com.lbin.server.system.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author mo
 * @date 2020/12/11
 */
@Component
public class MenuServer extends ComponentServer<Menu> {

    @Autowired
    private MenuService menuService;

}