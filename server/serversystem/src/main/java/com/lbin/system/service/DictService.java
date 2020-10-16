package com.lbin.system.service;

import com.lbin.jpa.service.BaseService;
import com.lbin.system.domain.Dict;

/**
 * @author lbin
 * @date 2019/10/31
 */
public interface DictService extends BaseService<Dict> {

    void reset();
}