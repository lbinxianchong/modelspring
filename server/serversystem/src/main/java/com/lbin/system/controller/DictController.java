package com.lbin.system.controller;



import com.lbin.common.util.ResultVoUtil;
import com.lbin.common.vo.ResultVo;
import com.lbin.jpa.controller.BaseController;
import com.lbin.system.domain.Dict;
import com.lbin.system.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import javax.annotation.PostConstruct;


@Controller
@RequestMapping("/system/dict")
public class DictController extends BaseController<Dict> {

    @Autowired
    private DictService dictService;


    @GetMapping("/reset")
    @ResponseBody
    public ResultVo reset() {
        dictService.reset();
        return ResultVoUtil.SAVE_SUCCESS;
    }
}
