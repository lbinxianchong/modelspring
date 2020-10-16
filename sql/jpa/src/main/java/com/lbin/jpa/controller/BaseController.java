package com.lbin.jpa.controller;



import com.lbin.common.util.ReflectUtil;
import com.lbin.common.util.ResultVoUtil;
import com.lbin.common.vo.ResultVo;
import com.lbin.jpa.enums.StatusEnum;
import com.lbin.jpa.service.BaseService;
import com.lbin.jpa.utils.EntityBeanUtil;
import com.lbin.jpa.utils.StatusUtil;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class BaseController<T> {


    protected BaseService<T> baseService;


    /**
     * 列表页面
     */
    @GetMapping("/index")
    public String index(Model model, T t, HttpServletRequest request) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.contains());

        // 获取数据列表
        Example<T> example = Example.of(t, matcher);
        Page<T> list = baseService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);

        return request.getServletPath();
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    public String toAdd(HttpServletRequest request) {
        return request.getServletPath();
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    public String toEdit(@PathVariable("id") T t, Model model,HttpServletRequest request) {
        model.addAttribute("model", t);
        String servletPath = request.getServletPath();
        servletPath=servletPath.substring(0,servletPath.lastIndexOf("/"));
        servletPath=servletPath.replaceAll("edit","add");
        return servletPath;
    }

    /**
     * 保存添加/修改的数据
     */
    @PostMapping("/save")
    @ResponseBody
    public ResultVo save(T t) {
        // 复制保留无需修改的数据
        Long id = (Long) ReflectUtil.getFieldValue(t, "id");
        if (id != null) {
            T be = baseService.getById(id);
            EntityBeanUtil.copyProperties(be, t);
        }

        // 保存数据
        baseService.save(t);
        return ResultVoUtil.SAVE_SUCCESS;
    }


    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    public String toDetail(@PathVariable("id") T t, Model model,HttpServletRequest request) {
        model.addAttribute("model", t);

        String servletPath = request.getServletPath();
        servletPath=servletPath.substring(0,servletPath.lastIndexOf("/"));
        return servletPath;
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @PostMapping("/status/{param}")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (baseService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}
