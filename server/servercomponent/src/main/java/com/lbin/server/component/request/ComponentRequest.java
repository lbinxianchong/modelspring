package com.lbin.server.component.request;


import com.lbin.common.util.HttpServletUtil;
import com.lbin.common.util.ResultVoUtil;
import com.lbin.common.vo.ResultVo;
import com.lbin.server.component.server.ComponentServer;
import com.lbin.server.component.util.InitUtil;
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
 *  请求
 * @param <T>   数据模型
 * @param <S>   数据模型Validated（BaseValid）
 *     @Autowired
 *     private ComponentServer componentServer;
 *
 */
public class ComponentRequest<T, S> {

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
        //RequestMapping初始化
        requestMapping = InitUtil.getRequestMapping(requestMapping, getClass());
        //BaseService初始化
        componentServer = InitUtil.getComponentServer(this);

    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ResponseBody
    public Object list(T t) {
        Map<String, Object> map = componentServer.list(t);
        return ResultVoUtil.success(map);
    }

    /**
     * 列表页面
     */
    @RequestMapping("/index")
    @ResponseBody
    public Object index(T t) {
        Map<String, Object> map = componentServer.index(t);
        return getRequest(getRequestMapping("/index"), map);
    }

    /**
     * 跳转到excel页面
     */
    @GetMapping("/excel")
    @ResponseBody
    public Object excel() {
        Map<String, Object> map = componentServer.excel();
        map.put("api",requestMapping);
        return getView("/system/component/excel", map);
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
    @ResponseBody
    public Object toAdd() {
        Map<String, Object> map = componentServer.toAdd();
        return getView(getRequestMapping("/add"), map);
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @ResponseBody
    public Object toEdit(@PathVariable("id") T t) {
        Map<String, Object> map = componentServer.toEdit(t);
        return getView(getRequestMapping("/edit"), map);
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
    @ResponseBody
    public Object toDetail(@PathVariable("id") T t, Model model) {
        Map<String, Object> map = componentServer.toDetail(t);
        return getRequest(getRequestMapping("/detail"), map);
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
