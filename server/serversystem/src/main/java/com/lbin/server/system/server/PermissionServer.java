package com.lbin.server.system.server;

import com.lbin.server.component.server.ComponentServer;
import com.lbin.server.system.domain.Permission;
import com.lbin.server.system.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PermissionServer extends ComponentServer<Permission> {
    @Autowired
    private PermissionService permissionService;
}
