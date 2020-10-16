package com.lbin.component.fileUpload.service;

import cn.hutool.core.util.StrUtil;
import com.lbin.common.config.FileProjectProperties;
import com.lbin.component.fileUpload.UploadUtil;
import com.lbin.component.fileUpload.data.Upload;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@Service
@Setter
@Getter
public class UploadService {

    @Autowired
    private FileProjectProperties uploadProjectProperties;


    public boolean delete(Upload upload) {
        return UploadUtil.delete(upload);
    }

    /**
     * addPrefix
     * 创建本地文件
     *
     * @param upload
     * @return
     */
    public Upload removePrefix(Upload upload) {
        String path = upload.getPath();
        path = StrUtil.removePrefix(path, uploadProjectProperties.getFilePath());
        upload.setPath(path);
        return upload;
    }

    /**
     * addPrefix
     * 创建本地文件
     *
     * @param upload
     * @return
     */
    public Upload addPrefix(Upload upload) {
        String path = upload.getPath();
        path = StrUtil.addPrefixIfNot(path, uploadProjectProperties.getFilePath());
        upload.setPath(path);
        return upload;
    }

    public Upload createFile(String path) {
        String prefix = uploadProjectProperties.getFilePath();
        return UploadUtil.createFile(path, prefix);
    }

    public String getFileSha1(MultipartFile multipartFile) {
        return UploadUtil.getFileSha1NoClose(multipartFile);
    }

    public Upload getFileUpload(MultipartFile multipartFile) {
        return getFileUpload(multipartFile, null);
    }

    public Upload getFileUpload(MultipartFile multipartFile, String prefixPath) {
        if (prefixPath == null || prefixPath.trim().length() == 0) {
            prefixPath = uploadProjectProperties.getUploadPath();
        }
        Upload upload = UploadUtil.getFileUpload(multipartFile,
                uploadProjectProperties.getFilePath() + prefixPath,
                uploadProjectProperties.getStaticPath() + prefixPath);
        return upload;
    }


    public Upload uploadFile(MultipartFile multipartFile) {
        return uploadFile(multipartFile, "");
    }

    public Upload uploadFile(MultipartFile multipartFile, String prefixPath) {
        Upload upload = getFileUpload(multipartFile, prefixPath);
        return uploadFile(multipartFile, upload);
    }

    public Upload uploadFile(MultipartFile multipartFile, Upload upload) {
        upload = UploadUtil.saveFileAndDigester(multipartFile, upload);
        return upload;
    }

    public Upload downloadFile(Upload upload, HttpServletResponse response) {
        return UploadUtil.downloadFile(upload, response);
    }


}
