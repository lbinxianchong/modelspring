package com.lbin.server.annotation;

import com.lbin.common.util.SpringContextUtil;
import com.lbin.common.vo.ResultVo;
import com.lbin.jpa.repository.BaseRepository;
import com.lbin.jpa.service.BaseService;
import com.lbin.jpa.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/model")
public class ModelController<T> {

    //    @Autowired
    protected ModelService modelService;

    protected String url = "/jpa/model";

    protected String modelapi = "model";

    /**
     * 列表页面
     */
    @GetMapping("/{entity}/index")
    public String index(
            Model model,
            @PathVariable("entity") String entity,
            T t) {
        model.mergeAttributes(modelApi(entity).index(t));
        return getUrl(model, entity, "/index");
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/{entity}/add")
    public String toAdd(
            Model model,
            @PathVariable("entity") String entity) {
        model.mergeAttributes(modelApi(entity).toAdd());
        return getUrl(model, entity, "/add");
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/{entity}/edit/{id}")
    public String toEdit(
            Model model,
            @PathVariable("entity") String entity,
            @PathVariable("id") Long id) {
        model.mergeAttributes(modelApi(entity).toEdit(id));
        return getUrl(model, entity, "/add");
    }

    /**
     * 保存添加/修改的数据
     */
    @PostMapping("/{entity}/save")
    @ResponseBody
    public ResultVo save(
            @PathVariable("entity") String entity,
            T t) {
        return modelApi(entity).save(t);
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/{entity}/detail/{id}")
    public String toDetail(
            Model model,
            @PathVariable("entity") String entity,
            @PathVariable("id") Long id) {
        model.mergeAttributes(modelApi(entity).toDetail(id));
        return getUrl(model, entity, "/detail");
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/{entity}/status/{param}")
    @ResponseBody
    public ResultVo status(
            @PathVariable("entity") String entity,
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        return modelApi(entity).status(param, ids);
    }

    private ModelService modelApi(String model) {
        if (modelService != null) {
            return modelService;
        }
        ModelService<T> modelService = null;
        try {
            modelService = SpringContextUtil.getBean(model + "Service", ModelService.class);
        } catch (Exception e) {
            BaseRepository bean = SpringContextUtil.getBean(model + "Repository", BaseRepository.class);
            BaseService baseService = new BaseServiceImpl(bean);
            modelService = new ModelService(baseService);
        }
        return modelService;
    }

    protected Model getModel(Model model, String entity) {
        model.addAttribute("model", modelapi);
        model.addAttribute("entity", entity);
        return model;
    }

    protected String getUrl(Model model, String entity, String api) {
        getModel(model, entity);
        return url + api;
    }
}
