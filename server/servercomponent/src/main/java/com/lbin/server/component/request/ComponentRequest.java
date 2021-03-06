package com.lbin.server.component.request;


import com.lbin.common.util.HttpServletUtil;
import com.lbin.common.util.ResultVoUtil;
import com.lbin.common.vo.ResultVo;
import com.lbin.server.component.server.ComponentServer;
import com.lbin.server.component.util.InitUtil;
import lombok.Getter;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 请求
 *
 * @param <T> 数据模型
 * @param <S> 数据模型Validated（BaseValid）
 * @Autowired private ComponentServer componentServer;
 */
@Getter
public class ComponentRequest<T, S> {

    //ControllerRequestMapping接口
    protected String requestMapping;

    protected String thymeleafPath;

    protected ComponentServer componentServer;

    /*内部方法*/

    /**
     * 获取RequestMapping接口
     *
     * @param url
     * @return
     */
    public String getThymeleafPath(String url) {
        return getThymeleafPath() + url;
    }

    /**
     * 判断api请求
     *
     * @return
     */
    public boolean requestApi() {
        HttpServletRequest request = HttpServletUtil.getRequest();
        String servletPath = request.getServletPath();
        return servletPath.startsWith("/api");
    }

    /**
     * 获取ModelAndView页面
     *
     * @param url
     * @param map
     * @return
     */
    public ModelAndView getModelAndView(String url, Map<String, Object> map) {
        if (getThymeleafPath().equals("/system/model")) {
            map.put("baseFieldModel", getComponentServer().getBaseFieldModel());
            map.put("api", getRequestMapping());
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addAllObjects(map);
        modelAndView.setViewName(url);
        return modelAndView;
    }

    /**
     * Request请求
     *
     * @param url
     * @param map
     * @return
     */
    public Object getRequest(String url, Map<String, Object> map) {
        if (requestApi()) {
            return ResultVoUtil.success(map);
        }
        return getModelAndView(url, map);
    }

    /**
     * View页面（Api不存在）
     *
     * @param url
     * @param map
     * @return
     */
    public Object getView(String url, Map<String, Object> map) {
        if (requestApi()) {
            return ResultVoUtil.API_ERROR;
        }
        return getModelAndView(url, map);
    }

    /*初始化*/

    @PostConstruct
    public void init() {
        //ThymeleafPath初始化
        thymeleafPath = InitUtil.getRequestMapping(thymeleafPath, getClass());
        //RequestMapping初始化
        requestMapping = InitUtil.getRequestMapping(requestMapping, getClass());
        //BaseService初始化
        componentServer = InitUtil.getComponentServer(this);

    }

    /**
     * 列表
     */
    @GetMapping("/list")
    @ResponseBody
    public ResultVo list() {
        return getComponentServer().list();
    }

    /**
     * 列表
     */
    @GetMapping("/findAll")
    @ResponseBody
    public ResultVo findAll(T t) {
        Map<String, Object> map = getComponentServer().findAll(t);
        return ResultVoUtil.success(map);
    }

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @ResponseBody
    public Object index(T t) {
        Map<String, Object> map = getComponentServer().index(t);
        return getRequest(getThymeleafPath("/index"), map);
    }

    /**
     * 跳转到excel页面
     */
    @GetMapping("/excel")
    @ResponseBody
    public Object excel() {
        Map<String, Object> map = getComponentServer().excel();
        map.put("api", getRequestMapping());
        return getView("/system/component/excel", map);
    }


    /**
     * 导入
     */
    @PostMapping("/importExcel")
    @ResponseBody
    public ResultVo importExcel(@RequestParam("file") MultipartFile multipartFile) {
        return getComponentServer().importExcel(multipartFile);
    }

    /**
     * 导出模板
     */
    @GetMapping("/exportExcelTitle")
    public void exportExcelTitle(HttpServletRequest request, HttpServletResponse response) {
        getComponentServer().exportExcelTitle(request, response);
    }

    /**
     * 导出
     */
    @GetMapping("/exportExcel")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
        getComponentServer().exportExcel(request, response);
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @ResponseBody
    public Object toAdd(T t) {
        Map<String, Object> map = getComponentServer().toAdd(t);
        return getView(getThymeleafPath("/add"), map);
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @ResponseBody
    public Object toEdit(@PathVariable("id") T t) {
        Map<String, Object> map = getComponentServer().toEdit(t);
        return getView(getThymeleafPath("/add"), map);
    }

    /**
     * 保存添加/修改的数据
     */
    @PostMapping("/save")
    @ResponseBody
    public ResultVo save(@Validated S s, @Validated T t) {
        return getComponentServer().save(t);
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @ResponseBody
    public Object toDetail(@PathVariable("id") T t, Model model) {
        Map<String, Object> map = getComponentServer().toDetail(t);
        return getRequest(getThymeleafPath("/detail"), map);
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @PostMapping("/status/{param}")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        return getComponentServer().status(param, ids);
    }

}
