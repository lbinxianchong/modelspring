package com.lbin.modelspring.runner;

import com.lbin.system.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 服务启动执行
 */

@Component
@Order(2)
@Slf4j
public class CacheRunner implements CommandLineRunner {

    @Autowired
    private DictService dictService;

    @Override
    public void run(String... args) throws Exception {
        log.info("服务启动执行，初始化数据开始");
        dictService.reset();
        log.info("服务启动执行，初始化数据结束");
    }
}
