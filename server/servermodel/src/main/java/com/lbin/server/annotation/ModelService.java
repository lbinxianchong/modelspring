package com.lbin.server.annotation;


import com.lbin.common.util.ReflectUtil;
import com.lbin.common.util.ResultVoUtil;

import com.lbin.common.vo.ResultVo;
import com.lbin.jpa.domain.BaseFieldModel;
import com.lbin.jpa.enums.StatusEnum;
import com.lbin.jpa.service.BaseService;
import com.lbin.jpa.utils.EntityBeanUtil;
import com.lbin.jpa.utils.StatusUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Component
@Setter
@Getter
public class ModelService<T> {

    protected BaseService<T> baseService;

    protected BaseFieldModel baseFieldModel;
    //备用Collection字段
    protected String url = "/jpa/model";
    //备用Collection字段
    protected String modelapi = "model";
    //备用Collection字段
    protected String entity = "BaseModel";


    public ModelService() {
    }

    public ModelService(BaseService<T> baseService) {
        this.baseService = baseService;
    }

    /**
     * 列表页面
     */
    public Map<String, Object> list() {
        Map<String, Object> model = new HashMap<>();
        List<T> list = getBaseService().findAll();
        model.put("list", list);
        return model;
    }

    /**
     * 列表页面
     */
    public Map<String, Object> list(T t) {
        Map<String, Object> model = new HashMap<>();
        List<T> list = getBaseService().findAll(t);
        model.put("list", list);
        return model;
    }

    /**
     * 列表页面
     */
    public Map<String, Object> list(Example<T> example) {
        Map<String, Object> model = new HashMap<>();
        List<T> list = getBaseService().findAll(example);
        model.put("list", list);
        return model;
    }

    /**
     * 列表页面
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
        Map<String, Object> model = new HashMap<>();
        Page<T> list = getBaseService().getPageList(t);

        // 封装数据
        model.put("list", list.getContent());
        model.put("page", list);
        model.put("searchList", baseFieldModel.getSearchList());
        model.put("titleList", baseFieldModel.getIndexList());
        model.put("fieldList", baseFieldModel.getIndexListID(list.getContent()));
        return model;
    }

    /**
     * 列表页面
     */
    public Map<String, Object> index(Example<T> example) {
        Map<String, Object> model = new HashMap<>();
        Page<T> list = getBaseService().getPageList(example);

        // 封装数据
        model.put("list", list.getContent());
        model.put("page", list);
        model.put("searchList", baseFieldModel.getSearchList());
        model.put("titleList", baseFieldModel.getIndexList());
        model.put("fieldList", baseFieldModel.getIndexListID(list.getContent()));
        return model;
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
        model.put("field", baseFieldModel.getAddListID());
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
        model.put("field", baseFieldModel.getAddListID(t));
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
        model.put("field", baseFieldModel.getDetailListID(t));
        return model;
    }

    /**
     * 保存添加/修改的数据
     */
    public ResultVo save(T t) {
        Long id = null;
        try {
            id = (Long) ReflectUtil.getFieldValue(t, t.getClass().getDeclaredField("id"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        // 复制保留无需修改的数据
        if (id != null) {
            T be = getBaseService().findById(id);
            EntityBeanUtil.copyProperties(be, t);
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
