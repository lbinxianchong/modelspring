package com.lbin.system.server;


import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.lbin.common.domain.BaseField;
import com.lbin.common.domain.BaseFieldModel;
import com.lbin.component.excel.ExcelUtils;
import com.lbin.component.fileUpload.UploadUtil;
import com.lbin.component.fileUpload.config.UploadProjectProperties;
import com.lbin.component.fileUpload.data.Upload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
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
     *
     * @param baseFieldModel
     * @return
     */
    public Upload exportExcelTitle(BaseFieldModel baseFieldModel, List<BaseField> baseFieldList) {
        return exportExcel(baseFieldModel, baseFieldList, null);
    }

    /**
     * 导出Excel数据
     *
     * @param baseFieldModel
     * @param list
     * @return
     */
    public Upload exportExcel(BaseFieldModel baseFieldModel, List<BaseField> baseFieldList, List<?> list) {
        String name = baseFieldModel.getName();
        Map<String, String> titleCommon = ExcelUtils.getTitleList(baseFieldList);
        //文件名字
        String filename = name + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN) + ".xlsx";
        String path =  dir + "/" + filename;
        Upload upload = UploadUtil.createFile(path,uploadProjectProperties.getFilePath());
        File file = ExcelUtils.exportExcel(name, titleCommon, list, upload.getFile());
        upload.setFile(file);
        return upload;
    }

    /**
     * 导入Excel数据
     *
     * @return
     */
    public <T> List<T> importExcel(BaseFieldModel baseFieldModel, List<BaseField> baseFieldList, Upload upload) {
        File file = upload.getFile();
        Map<String, String> titleCommon = ExcelUtils.getFieldList(baseFieldList);
        List<T> list = ExcelUtils.importExcel(baseFieldModel.getEntity(), titleCommon, FileUtil.getInputStream(file));
        return list;
    }
}
