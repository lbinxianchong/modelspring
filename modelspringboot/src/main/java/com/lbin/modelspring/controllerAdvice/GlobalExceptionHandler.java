package com.lbin.modelspring.controllerAdvice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView customException(Exception e) {
        ModelAndView model = new ModelAndView();
        model.addObject("message", e.getMessage());
        model.setViewName("/error");
        return model;
    }


}
