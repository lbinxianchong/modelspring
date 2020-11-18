package com.lbin.server.model.annotation;

import com.lbin.component.fileUpload.service.UploadService;
import com.lbin.server.component.request.ComponentRequest;
import com.lbin.server.component.server.ComponentServer;
import com.lbin.server.component.service.ComponentService;
import com.lbin.server.component.util.InitUtil;
import com.lbin.sql.jpa.repository.BaseRepository;
import com.lbin.sql.jpa.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

public class ModelRequest<T, S> extends ComponentRequest<T, S> {

    protected BaseRepository<T, Long> baseRepository;

    @Autowired
    protected UploadService uploadService;

    @Autowired
    protected ComponentService componentService;

    @Override
    public void init() {
        thymeleafPath = "/system/model";
        baseRepository= InitUtil.getBaseRepository(this);
        requestMapping = InitUtil.getRequestMapping(requestMapping, getClass());
        //BaseService初始化
        componentServer = InitUtil.getComponentServer(this);
        if (componentServer==null){
            componentServer = new ComponentServer();
            componentServer.setBaseService(new BaseServiceImpl<T>(baseRepository));
            componentServer.setBaseFieldModel(InitUtil.getBaseFieldModel(getClass()));
            componentServer.setUploadService(uploadService);
            componentServer.setComponentService(componentService);
        }
    }
}
