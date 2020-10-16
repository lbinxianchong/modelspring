package com.lbin.swagger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author
 * @date 2018/12/9
 */
@Controller
public class SwaggerController {

    @GetMapping("/dev/swagger")
    public String index(){
        return "redirect:/swagger-ui.html";
    }
}
