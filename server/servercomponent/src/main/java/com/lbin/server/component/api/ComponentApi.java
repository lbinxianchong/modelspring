package com.lbin.server.component.api;

import com.lbin.common.util.ResultVoUtil;
import com.lbin.common.vo.ResultVo;
import com.lbin.server.component.server.ComponentServer;
import com.lbin.server.component.util.InitUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComponentApi<T, S> {

    protected ComponentServer componentServer;

    @PostConstruct
    public void init() {
        componentServer= InitUtil.getComponentServer(this);
    }

    /**
     * 列表页面
     */
    @GetMapping("/index")
    public ResultVo index(T t) {
        Map<String, Object> map = componentServer.index(t);
        return ResultVoUtil.success(map);
    }
    /**
     * 导入
     */
    @PostMapping("/importExcel")
    @ResponseBody
    public ResultVo importExcel(MultipartFile multipartFile) {
        return componentServer.importExcel(multipartFile);
    }

    /**
     * 导出模板
     */
    @GetMapping("/exportExcelTitle")
    public void exportExcelTitle(HttpServletRequest request, HttpServletResponse response) {
        componentServer.exportExcelTitle(request, response);
    }

    /**
     * 导出
     */
    @GetMapping("/exportExcel")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
        componentServer.exportExcel(request, response);
    }


    /**
     * 保存添加/修改的数据
     */
    @PostMapping("/save")
    @ResponseBody
    public ResultVo save(@Validated S s, @Validated T t) {
        return componentServer.save(t);
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    public ResultVo toDetail(@PathVariable("id") T t) {
        Map<String,Object> model=new HashMap<>();
        model.put("model", t);
        return ResultVoUtil.success(model);
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @PostMapping("/status/{param}")
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        return componentServer.status(param, ids);
    }
}
