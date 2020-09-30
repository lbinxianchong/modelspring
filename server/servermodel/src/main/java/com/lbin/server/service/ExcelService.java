package com.lbin.server.service;


import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.lbin.common.domain.BaseFieldModel;
import com.lbin.component.excel.ExcelUtils;
import com.lbin.component.fileUpload.UploadUtil;
import com.lbin.component.fileUpload.config.UploadProjectProperties;
import com.lbin.component.fileUpload.data.Upload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ExcelService {

    @Autowired
    private UploadProjectProperties uploadProjectProperties;

    private final static String dir = "/excel";

    /**
     * 模板（仅输出标题）
     * @param baseFieldModel
     * @return
     */
    public Upload exportExcelTitle(BaseFieldModel baseFieldModel) {
        return exportExcel(baseFieldModel, new ArrayList<>());
    }

    /**
     * 导出Excel数据
     * @param baseFieldModel
     * @param list
     * @return
     */
    public Upload exportExcel(BaseFieldModel baseFieldModel, List<?> list) {
        String name = baseFieldModel.getName();
        Map<String, String> titleCommon = ExcelUtils.getTitleCommon(baseFieldModel.getDetailList());
        //文件名字
        String filename = name + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN) + ".xlsx";
        String path = uploadProjectProperties.getFilePath() + dir + "/" + filename;
        Upload upload = UploadUtil.createFile(path);
        File file = ExcelUtils.exportExcel(upload.getFile(), name, titleCommon, list);
        upload.setFile(file);
        return upload;
    }

    /**
     * 导出Excel数据
     * @return
     */
    public <T> List<T> importExcel(Class<T> entity,Upload upload) {
        File file = upload.getFile();
        List<T> list = ExcelUtils.importExcel(entity, FileUtil.getInputStream(file));
        return list;
    }
}
