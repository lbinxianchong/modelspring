package com.lbin.model.service;

import com.lbin.common.domain.BaseFieldModel;
import com.lbin.jpa.service.BaseService;
import com.lbin.jpa.service.impl.BaseServiceImpl;
import com.lbin.model.domain.ExampleModel;
import com.lbin.model.repository.ExampleModelRepository;
import com.lbin.server.annotation.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class ExampleModelService extends ModelService<ExampleModel> {

    @Autowired
    private ExampleModelRepository exampleModelRepository;

    @PostConstruct
    public void init() {
        baseFieldModel = new BaseFieldModel(ExampleModel.class);
        baseService = new BaseServiceImpl<ExampleModel>(exampleModelRepository);
    }
}
