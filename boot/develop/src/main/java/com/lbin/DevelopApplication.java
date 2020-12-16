package com.lbin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;


/**
 * @author
 * @date 2018/8/14
 */
//排除api
@SpringBootApplication
public class DevelopApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(DevelopApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DevelopApplication.class);
    }
}
