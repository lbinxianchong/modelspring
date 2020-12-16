package com.lbin.component.thymeleaf.config;

import com.lbin.component.thymeleaf.dialect.ThymeleafDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author
 * @date 2018/8/14
 */
@Configuration
public class ThymeleafConfig {

    /**
     * 配置自定义的CusDialect，用于整合thymeleaf模板
     */
    @Bean
    public ThymeleafDialect getThymeleafDialect(){
        return new ThymeleafDialect();
    }
}
