package com.lbin.model.service;

import com.lbin.server.component.server.ComponentServer;
import com.lbin.model.domain.ExampleModel;
import com.lbin.model.repository.ExampleModelRepository;
import com.lbin.server.model.annotation.ModelServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//@Service
public class ExampleModelServer extends ModelServer<ExampleModel> {

    @Autowired
    private ExampleModelRepository exampleModelRepository;

}
