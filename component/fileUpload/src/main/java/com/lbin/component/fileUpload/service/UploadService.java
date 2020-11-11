package com.lbin.component.fileUpload.service;

import cn.hutool.core.util.StrUtil;
import com.lbin.common.config.FileProjectProperties;
import com.lbin.common.exception.ResultException;
import com.lbin.component.fileUpload.UploadUtil;
import com.lbin.component.fileUpload.data.Upload;
import com.lbin.component.fileUpload.enums.UploadResultEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

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
        return UploadUtil.removePrefixPath(upload,uploadProjectProperties.getFilePath());
    }

    /**
     * addPrefix
     * 创建本地文件
     *
     * @param upload
     * @return
     */
    public Upload addPrefix(Upload upload) {
        return UploadUtil.addPrefixPath(upload,uploadProjectProperties.getFilePath());
    }

    public Upload file(String path) {
        String prefix = uploadProjectProperties.getFilePath();
        return UploadUtil.file(path, prefix);
    }

    /**
     * 获取文件的SHA1值
     */
    public String getFileSha1(MultipartFile multipartFile) {
        String s = null;
        InputStream inputStream = null;
        try {
            inputStream = multipartFile.getInputStream();
            s = UploadUtil.getFileSha1NoClose(inputStream);
            inputStream.close();
        } catch (IOException e) {
            throw new ResultException(UploadResultEnum.NO_FILE_Sha1);
        }
        return s;
    }

    /**
     * 创建一个Upload实体对象
     * 获取上传对象
     *
     * @param multipartFile MultipartFile对象
     */
    public Upload getFileUpload(MultipartFile multipartFile, String prefixPath) {
        if (multipartFile.getSize() == 0) {
            throw new ResultException(UploadResultEnum.NO_FILE_NULL);
        }
        if (prefixPath == null || prefixPath.trim().length() == 0) {
            prefixPath = uploadProjectProperties.getUploadPath();
        }
        String name = multipartFile.getName();
        String modulePath = uploadProjectProperties.getFilePath() + prefixPath;
        String baseUrl = uploadProjectProperties.getStaticPath() + prefixPath;
        Upload upload = UploadUtil.getFileUpload(name,modulePath,baseUrl);
        upload.setMime(multipartFile.getContentType());
        upload.setSize(multipartFile.getSize());

        return upload;
    }

    public Upload getFileUpload(MultipartFile multipartFile) {
        return getFileUpload(multipartFile, null);
    }

    public Upload uploadFile(MultipartFile multipartFile) {
        return uploadFile(multipartFile, "");
    }

    public Upload uploadFile(MultipartFile multipartFile, String prefixPath) {
        Upload upload = getFileUpload(multipartFile, prefixPath);
        return uploadFile(multipartFile, upload);
    }

    /**
     * 保存文件及获取文件MD5值和SHA1值
     *
     * @param upload Upload
     */
    public Upload uploadFile(MultipartFile multipartFile, Upload upload) {
        try {
            InputStream inputStream = multipartFile.getInputStream();
            upload = UploadUtil.saveFileAndDigester(inputStream, upload);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return upload;
    }

    public Upload downloadFile(Upload upload, HttpServletResponse response) {
        upload = UploadUtil.getLocalFile(upload);
        return UploadUtil.downloadFile(upload, response);
    }


}
