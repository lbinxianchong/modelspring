package com.lbin.system.controller;


import com.lbin.common.vo.ResultVo;
import com.lbin.server.component.request.ComponentRequest;
import com.lbin.server.system.server.DictServer;
import com.lbin.server.system.domain.Dict;
import com.lbin.system.validator.DictValid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping({"/system/dict", "/api/system/dict"})
public class DictController extends ComponentRequest<Dict, DictValid> {

    @Autowired
    private DictServer dictServer;

    @GetMapping("/reset")
    @ResponseBody
    public ResultVo reset() {
        return dictServer.reset();
    }

}
