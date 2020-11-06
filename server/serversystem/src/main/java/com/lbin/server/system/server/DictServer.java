package com.lbin.server.system.server;

import com.lbin.common.util.ResultVoUtil;
import com.lbin.common.vo.ResultVo;
import com.lbin.server.component.server.ComponentServer;
import com.lbin.server.system.domain.Dict;
import com.lbin.server.system.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DictServer extends ComponentServer<Dict> {

    @Autowired
    private DictService dictService;

    public ResultVo reset() {
        dictService.reset();
        return ResultVoUtil.SAVE_SUCCESS;
    }
}
