package com.lbin.server.component.controller;

import com.lbin.common.domain.BaseField;
import com.lbin.common.domain.BaseFieldModel;
import com.lbin.common.util.ReflectUtil;
import com.lbin.common.util.ResultVoUtil;
import com.lbin.common.vo.ResultVo;
import com.lbin.server.component.util.InitUtil;
import com.lbin.sql.jpa.enums.StatusEnum;
import com.lbin.sql.jpa.service.BaseService;
import com.lbin.common.util.EntityBeanUtil;
import com.lbin.sql.jpa.utils.StatusUtil;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;

import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Getter
@Setter
public class BaseController<T, S> {
    //BaseService实现类
    protected BaseService<T> baseService;
    //BaseFieldModel模版
    protected BaseFieldModel baseFieldModel;
    //ControllerRequestMapping接口
    protected String requestMapping = "/system/model";

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
        //BaseFieldModel初始化
        baseFieldModel = InitUtil.getBaseFieldModel(getClass());
        //RequestMapping初始化
        requestMapping = InitUtil.getRequestMapping(requestMapping, getClass());
        //BaseService初始化
        baseService = InitUtil.getBaseService(this);
    }

    /**
     * 列表页面
     */
    @GetMapping("/index")
    public String index(Model model, T t, HttpServletRequest request) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching();
        List<BaseField> searchList = getBaseFieldModel().getSearchList();
        for (BaseField baseField : searchList) {
            matcher.withMatcher(baseField.getName(), match -> match.contains());
        }

        // 获取数据列表
        Example<T> example = Example.of(t, matcher);
        Page<T> list = baseService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return getRequestMapping("/index");
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    public String toAdd(HttpServletRequest request) {
        return getRequestMapping("/add");
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    public String toEdit(@PathVariable("id") T t, Model model, HttpServletRequest request) {
        model.addAttribute("model", t);
        return getRequestMapping("/edit");
    }

    /**
     * 保存添加/修改的数据
     */
    @PostMapping("/save")
    @ResponseBody
    public ResultVo save(@Validated S s, @Validated T t) {
        return save(t);
    }

    protected ResultVo save(T t) {
        // 复制保留无需修改的数据
        Long id = (Long) ReflectUtil.getFieldValue(t, "id");
        if (id != null) {
            T be = baseService.getById(id);
            EntityBeanUtil.copyProperties(be, t, getBaseFieldModel().getIgnoresList());
        }

        // 保存数据
        baseService.save(t);
        return ResultVoUtil.SAVE_SUCCESS;
    }


    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    public String toDetail(@PathVariable("id") T t, Model model, HttpServletRequest request) {
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
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (baseService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}
