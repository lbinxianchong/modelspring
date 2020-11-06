package com.lbin.server.component.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.lbin.common.domain.BaseField;
import com.lbin.common.domain.BaseFieldModel;
import com.lbin.component.excel.ExcelUtils;
import com.lbin.component.fileUpload.UploadUtil;
import com.lbin.component.fileUpload.data.Upload;
import com.lbin.component.fileUpload.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 组件Service
 */
@Service
public class ComponentService {

    @Autowired
    protected UploadService uploadService;

    public <T> List<T> importExcel(BaseFieldModel baseFieldModel,
                                   List<BaseField> baseFieldList,
                                   MultipartFile multipartFile) {
        Upload upload = uploadService.uploadFile(multipartFile);
        List<T> list = ExcelUtils.importExcel(baseFieldModel, baseFieldList, upload.getFile());
        return list;
    }

    /**
     * 导出Excel
     */
    public void exportExcel(BaseFieldModel baseFieldModel,
                              List<BaseField> baseFieldList,
                              List<?> list,
                              HttpServletRequest request,
                              HttpServletResponse response) {
        Upload upload = exportExcelUpload(baseFieldModel, baseFieldList, list);
        uploadService.downloadFile(upload, response);
    }

    /**
     * 导出Excel数据
     */
    private Upload exportExcelUpload(BaseFieldModel baseFieldModel,
                               List<BaseField> baseFieldList,
                               List<?> list) {
        String name = baseFieldModel.getName();
        Map<String, String> titleCommon = ExcelUtils.getTitleList(baseFieldList);
        //文件名字
        String filename = name + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN) + ".xlsx";
        String path = uploadService.getUploadProjectProperties().getExcelPath() + "/" + filename;
        Upload upload = UploadUtil.createFile(path, uploadService.getUploadProjectProperties().getFilePath());
        File file = ExcelUtils.exportExcel(name, titleCommon, list, upload.getFile());
        upload.setFile(file);
        return upload;
    }
}
