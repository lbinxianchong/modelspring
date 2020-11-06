package com.lbin.server.system.service;

import com.lbin.sql.jpa.service.BaseService;
import com.lbin.server.system.domain.Dict;

/**
 * @author lbin
 * @date 2019/10/31
 */
public interface DictService extends BaseService<Dict> {

    /**
     * 重置字典缓存
     */
    void reset();
}