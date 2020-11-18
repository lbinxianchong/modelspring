package com.lbin.server.model.annotation;

import com.lbin.server.component.server.ComponentServer;
import com.lbin.server.component.util.InitUtil;
import com.lbin.sql.jpa.repository.BaseRepository;
import com.lbin.sql.jpa.service.BaseService;
import com.lbin.sql.jpa.service.impl.BaseServiceImpl;
import lombok.Getter;
import lombok.Setter;

//@Component
@Setter
@Getter
public class ModelServer<T> extends ComponentServer<T> {

    protected BaseRepository<T, Long> baseRepository;

    @Override
    public void init() {
        baseRepository = InitUtil.getBaseRepository(this);
        baseService = new BaseServiceImpl<T>(baseRepository);
        //BaseFieldModel初始化
        baseFieldModel = InitUtil.getBaseFieldModel(getClass());
    }
}
