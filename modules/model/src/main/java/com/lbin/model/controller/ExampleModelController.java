package com.lbin.model.controller;

import com.lbin.model.domain.ExampleModel;
import com.lbin.model.service.ExampleModelService;
import com.lbin.server.annotation.ModelController;
import com.lbin.server.annotation.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/example")
public class ExampleModelController extends ModelController<ExampleModel> {

    public ExampleModelController() {
        url = "/jpa/model";
        modelapi = "example";
    }

    @Autowired
    private ExampleModelService exampleModelService;

    @Override
    protected ModelService modelApi(String model) {
        if (modelService == null) {
            modelService = exampleModelService;
        }
        return modelService;
    }
}
