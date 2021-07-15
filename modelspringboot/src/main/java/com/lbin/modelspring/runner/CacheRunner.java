package com.lbin.modelspring.runner;

import com.lbin.server.system.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * ��������ִ��
 */

@Component
@Order(2)
@Slf4j
public class CacheRunner implements CommandLineRunner {

    @Autowired
    private DictService dictService;

    @Override
    public void run(String... args) throws Exception {
        log.info("��������ִ�У���ʼ�����ݿ�ʼ");
        dictService.reset();
        log.info("��������ִ�У���ʼ�����ݽ���");
    }
}
