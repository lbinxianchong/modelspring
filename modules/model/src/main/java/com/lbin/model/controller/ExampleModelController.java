package com.lbin.model.controller;

import com.lbin.model.domain.ExampleModel;
import com.lbin.model.repository.ExampleModelRepository;
import com.lbin.model.service.ExampleModelServer;
import com.lbin.server.model.annotation.ModelController;
import com.lbin.server.model.annotation.ModelRequest;
import com.lbin.sql.jpa.validator.BaseValid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/example/exampleModel")
public class ExampleModelController extends ModelRequest<ExampleModel, BaseValid> {

    //    @Autowired
//    private ExampleModelServer exampleModelServer;
    @Autowired
    private ExampleModelRepository exampleModelRepository;

}
