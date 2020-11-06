package com.lbin.model.controller;

import com.lbin.model.domain.ExampleModel;
import com.lbin.model.service.ExampleModelService;
import com.lbin.server.model.annotation.ModelController;
import com.lbin.server.model.annotation.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;

@Controller
@RequestMapping("/example")
public class ExampleModelController extends ModelController<ExampleModel> {

    @Autowired
    private ExampleModelService exampleModelService;

    @PostConstruct
    public void init() {
        url = "/jpa/model";
        modelapi = "example";
        modelService = exampleModelService;
    }

}
