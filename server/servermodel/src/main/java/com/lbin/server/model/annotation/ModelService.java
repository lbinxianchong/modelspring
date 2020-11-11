package com.lbin.server.model.annotation;


import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.lbin.common.domain.BaseField;
import com.lbin.common.util.ReflectUtil;
import com.lbin.common.util.ResultVoUtil;

import com.lbin.common.vo.ResultVo;
import com.lbin.common.domain.BaseFieldModel;
import com.lbin.component.excel.ExcelUtils;
import com.lbin.component.fileUpload.UploadUtil;
import com.lbin.component.fileUpload.data.Upload;
import com.lbin.sql.jpa.enums.StatusEnum;
import com.lbin.sql.jpa.service.BaseService;
import com.lbin.common.util.EntityBeanUtil;
import com.lbin.sql.jpa.utils.StatusUtil;
import com.lbin.component.fileUpload.service.UploadService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Component
@Setter
@Getter
public class ModelService<T> {


    protected BaseService<T> baseService;

    @Autowired
    protected UploadService uploadService;

    protected BaseFieldModel baseFieldModel;


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
        model.put("title", getBaseFieldModel().getName());
        model.put("searchList", getBaseFieldModel().getSearchList(t));
        model.put("titleList", getBaseFieldModel().getIndexList());
        model.put("fieldList", getBaseFieldModel().getIndexListID(list.getContent()));
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
        model.put("title", getBaseFieldModel().getName());
        model.put("searchList", getBaseFieldModel().getSearchList());
        model.put("titleList", getBaseFieldModel().getIndexList());
        model.put("fieldList", getBaseFieldModel().getIndexListID(list.getContent()));
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
     * 导入Excel数据
     */
    public Map<String, Object> importExcel(MultipartFile multipartFile) {
        Map<String, Object> model = new HashMap<>();
        Upload upload = uploadService.uploadFile(multipartFile);
        List<T> list = ExcelUtils.importExcel(getBaseFieldModel(), getBaseFieldModel().getDetailList(), upload.getFile());
        for (T t : list) {
            save(t);
        }
        model.put("list", list);
        model.put("upload", upload);
        return model;
    }

    /**
     * 标题模板
     */
    public void exportExcelTitle(HttpServletResponse response) {
        Upload upload = exportExcel(getBaseFieldModel(), getBaseFieldModel().getDetailList(), null);
        uploadService.downloadFile(upload, response);
    }

    /**
     * 导出Excel数据
     */
    public void exportExcel(HttpServletResponse response) {
        List<T> list = getBaseService().findAll();
        Upload upload = exportExcel(getBaseFieldModel(), getBaseFieldModel().getDetailList(), list);
        uploadService.downloadFile(upload, response);
    }

    /**
     * 导出Excel数据
     */
    private Upload exportExcel(BaseFieldModel baseFieldModel, List<BaseField> baseFieldList, List<?> list) {
        String name = baseFieldModel.getName();
        Map<String, String> titleCommon = ExcelUtils.getTitleList(baseFieldList);
        //文件名字
        String filename = name + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN) + ".xlsx";
        String path = uploadService.getUploadProjectProperties().getExcelPath() + "/" + filename;
        Upload upload = UploadUtil.file(path, uploadService.getUploadProjectProperties().getFilePath());
        File file = ExcelUtils.exportExcel(name, titleCommon, list, upload.getFile());
        upload.setFile(file);
        return upload;
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
        model.put("field", getBaseFieldModel().getAddListID());
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
        model.put("field", getBaseFieldModel().getAddListID(t));
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
        model.put("field", getBaseFieldModel().getDetailListID(t));
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
