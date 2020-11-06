package com.lbin.server.component.controller;


import com.lbin.common.vo.ResultVo;
import com.lbin.server.component.server.ComponentServer;
import com.lbin.server.component.util.InitUtil;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * web页面（后台模版）
 * @param <T>   数据模型
 * @param <S>   数据模型Validated（BaseValid）
 */
public class ComponentController<T, S> {

    //ControllerRequestMapping接口
    protected String requestMapping = "/system/model";

    protected ComponentServer componentServer;

    /*内部方法*/

    /**
     * 获取RequestMapping接口
     *
     * @param url
     * @return
     */
    public String getRequestMapping(String url) {
        return requestMapping + url;
    }

    /*初始化*/

    @PostConstruct
    public void init() {
        //RequestMapping初始化
        requestMapping = InitUtil.getRequestMapping(requestMapping, getClass());

        componentServer= InitUtil.getComponentServer(this);
    }

    /**
     * 列表页面
     */
    @GetMapping("/index")
    public String index(Model model, T t) {
        model.mergeAttributes(componentServer.index(t));
        return getRequestMapping("/index");
    }

    /**
     * 跳转到excel页面
     */
    @GetMapping("/excel")
    public String excel(Model model) {
        model.mergeAttributes(componentServer.excel());
        return getRequestMapping("/excel");
    }


    /**
     * 导入
     */
    @PostMapping("/importExcel")
    @ResponseBody
    public ResultVo importExcel(@RequestParam("file") MultipartFile multipartFile) {
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
     * 跳转到添加页面
     */
    @GetMapping("/add")
    public String toAdd() {
        return getRequestMapping("/add");
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    public String toEdit(@PathVariable("id") T t, Model model) {
        model.addAttribute("model", t);
        return getRequestMapping("/edit");
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
    public String toDetail(@PathVariable("id") T t, Model model) {
        model.addAttribute("model", t);
        return getRequestMapping("/detail");
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @PostMapping("/status/{param}")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        return componentServer.status(param, ids);
    }


}
