package com.lbin.server.component.server;


import com.lbin.common.domain.BaseField;
import com.lbin.common.domain.BaseFieldModel;
import com.lbin.common.util.EntityBeanUtil;
import com.lbin.common.util.ReflectUtil;
import com.lbin.common.util.ResultVoUtil;
import com.lbin.common.vo.ResultVo;
import com.lbin.component.fileUpload.service.UploadService;
import com.lbin.server.component.service.ComponentService;
import com.lbin.server.component.util.InitUtil;
import com.lbin.sql.jpa.enums.StatusEnum;
import com.lbin.sql.jpa.service.BaseService;
import com.lbin.sql.jpa.utils.StatusUtil;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务
 * @param <T>   数据模型
 *     @Autowired
 *     private BaseService baseService;
 */
@Getter
public class ComponentServer<T> {

    //BaseService实现类
    protected BaseService<T> baseService;
    //BaseFieldModel模版
    protected BaseFieldModel baseFieldModel;

    /*Service*/
    @Autowired
    protected UploadService uploadService;

    @Autowired
    protected ComponentService componentService;

    /*初始化*/

    @PostConstruct
    public void init() {

        //BaseFieldModel初始化
        baseFieldModel = InitUtil.getBaseFieldModel(getClass());
        //BaseService初始化
        baseService = InitUtil.getBaseService(this);

    }

    /**
     * 列表
     */
    public Map<String, Object> list() {
        Map<String, Object> model = new HashMap<>();
        List<T> list = getBaseService().findAll();
        model.put("list", list);
        return model;
    }

    /**
     * 列表
     */
    public Map<String, Object> list(T t) {
        Map<String, Object> model = new HashMap<>();
        List<T> list = getBaseService().findAll(t);
        model.put("list", list);
        return model;
    }

    /**
     * 列表
     */
    public Map<String, Object> list(Example<T> example) {
        Map<String, Object> model = new HashMap<>();
        List<T> list = getBaseService().findAll(example);
        model.put("list", list);
        return model;
    }

    /**
     * 列表
     */
    public Map<String, Object> list(List<Long> longs) {
        Map<String, Object> model = new HashMap<>();
        List<T> list = getBaseService().findAllById(longs);
        model.put("list", list);
        return model;
    }

    /**
     * 列表页面
     */
    public Map<String, Object> index(T t) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching();
        List<BaseField> searchList = getBaseFieldModel().getSearchList();
        for (BaseField baseField : searchList) {
            matcher.withMatcher(baseField.getName(), match -> match.contains());
        }

        // 获取数据列表
        Example<T> example = Example.of(t, matcher);
        return getPageList(example);
    }

    /**
     * 列表页面
     */
    public Map<String, Object> getPageList(T t) {
        Map<String, Object> model = new HashMap<>();
        Page<T> list = getBaseService().getPageList(t);

        // 封装数据
        model.put("list", list.getContent());
        model.put("page", list);
        return model;
    }

    /**
     * 列表页面
     */
    public Map<String, Object> getPageList(Example<T> example) {
        Map<String, Object> model = new HashMap<>();
        Page<T> list = getBaseService().getPageList(example);

        // 封装数据
        model.put("list", list.getContent());
        model.put("page", list);
        return model;
    }

    /**
     * 导入Excel数据
     */
    public Map<String, Object> excel() {
        Map<String, Object> model = new HashMap<>();
        return model;
    }

    /**
     * 导出模板
     */
    public ResultVo importExcel(MultipartFile multipartFile) {
        try {
            List<T> list = componentService.importExcel(getBaseFieldModel(), getBaseFieldModel().getDetailList(), multipartFile);
            for (T t : list) {
                save(t);
            }
            return ResultVoUtil.success("导入成功");
        } catch (Exception e) {
            return ResultVoUtil.error("导入失败");
        }
    }

    /**
     * 导出模板
     */
    public void exportExcelTitle(HttpServletRequest request, HttpServletResponse response) {
        componentService.exportExcel(getBaseFieldModel(), getBaseFieldModel().getDetailList(), null, request, response);
    }

    /**
     * 导出
     */
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
        List<T> list = getBaseService().findAll();
        componentService.exportExcel(getBaseFieldModel(), getBaseFieldModel().getDetailList(), list, request, response);
    }

    /**
     * 判断实例存在
     *
     * @param id 主键ID
     * @return
     */
    public boolean exists(Long id) {
        return getBaseService().existsById(id);
    }

    /**
     * 判断实例存在
     *
     * @param t 查询实体对象
     * @return
     */
    public boolean exists(T t) {
        return getBaseService().exists(t);
    }

    /**
     * 获取数据数量
     *
     * @param t 查询实体对象
     * @return
     */
    public Long count(T t) {
        return getBaseService().count(t);
    }

    /**
     * 获取数据数量
     *
     * @param example 查询实例
     * @return
     */
    public Long count(Example<T> example) {
        return getBaseService().count(example);
    }

    /**
     * 根据ID查询数据
     *
     * @param id 主键ID
     */
    public T findOne(Long id) {
        return getBaseService().findById(id);
    }

    /**
     * 根据实体对象查询数据
     *
     * @param t 实体对象
     */
    public T findOne(T t) {
        return getBaseService().findOne(t);
    }

    /**
     * 跳转到添加页面
     */
    public Map<String, Object> toAdd() {
        Map<String, Object> model = new HashMap<>();
        return model;
    }

    /**
     * 跳转到编辑页面
     */
    public Map<String, Object> toEdit(Long id) {
        return toEdit(findOne(id));
    }

    public Map<String, Object> toEdit(T t) {
        Map<String, Object> model = new HashMap<>();
        model.put("model", t);
        return model;
    }

    /**
     * 跳转到详细页面
     */
    public Map<String, Object> toDetail(Long id) {
        return toDetail(findOne(id));
    }

    public Map<String, Object> toDetail(T t) {
        Map<String, Object> model = new HashMap<>();
        model.put("model", t);
        return model;
    }

    /**
     * 保存添加/修改的数据
     */
    public ResultVo save(T t) {
        Long id = (Long) ReflectUtil.getFieldValue(t, "id");
        // 复制保留无需修改的数据
        if (id != null) {
            T be = getBaseService().findById(id);
            EntityBeanUtil.copyProperties(be, t, getBaseFieldModel().getIgnoresList());
        }

        // 保存数据
        getBaseService().save(t);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 根据ID删除数据
     */
    public ResultVo delete(Long id) {
        getBaseService().deleteById(id);
        return ResultVoUtil.DELETE_SUCCESS;
    }

    /**
     * 根据实体对象删除数据
     */
    public ResultVo delete(T t) {
        getBaseService().delete(t);
        return ResultVoUtil.DELETE_SUCCESS;
    }

    /**
     * 删除所有数据
     */
    public ResultVo deleteAll() {
        getBaseService().deleteAll();
        return ResultVoUtil.DELETE_SUCCESS;
    }

    /**
     * 设置一条或者多条数据的状态
     */
    public ResultVo status(String param, List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (getBaseService().updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}
