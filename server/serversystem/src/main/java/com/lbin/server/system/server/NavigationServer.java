package com.lbin.server.system.server;

import com.lbin.server.component.server.ComponentServer;
import com.lbin.server.system.domain.Navigation;
import com.lbin.server.system.service.NavigationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author mo
 * @date 2020/12/11
 */
@Component
public class NavigationServer extends ComponentServer<Navigation> {

    @Autowired
    private NavigationService navigationService;

}