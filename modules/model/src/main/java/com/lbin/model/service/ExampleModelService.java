package com.lbin.model.service;

import com.lbin.jpa.domain.BaseFieldModel;
import com.lbin.jpa.service.BaseService;
import com.lbin.jpa.service.impl.BaseServiceImpl;
import com.lbin.model.domain.ExampleModel;
import com.lbin.model.repository.ExampleModelRepository;
import com.lbin.server.annotation.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ExampleModelService extends ModelService<ExampleModel> {

    @Autowired
    @Qualifier("exampleModelRepository")
    private ExampleModelRepository exampleModelRepository;

    public ExampleModelService() {
        baseFieldModel = new BaseFieldModel(ExampleModel.class);
    }

    @Override
    public BaseService<ExampleModel> getBaseService() {
        if (baseService == null) {
            baseService = new BaseServiceImpl<ExampleModel>(exampleModelRepository);
        }
        return baseService;
    }
}
