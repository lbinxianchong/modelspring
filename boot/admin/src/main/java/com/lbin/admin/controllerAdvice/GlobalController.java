package com.lbin.admin.controllerAdvice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalController {

    @ModelAttribute(name = "global")
    public Map<String,Object> mydata() {
        Map<String, Object> map = new HashMap<>();
        map.put("mainname","mo");
        return map;
    }
}
