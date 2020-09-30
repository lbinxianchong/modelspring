package com.lbin.server.service;

import com.lbin.component.fileUpload.UploadUtil;
import com.lbin.component.fileUpload.config.UploadProjectProperties;
import com.lbin.component.fileUpload.data.Upload;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

@Service
@Setter
@Getter
public class FileService {

    @Autowired
    private UploadProjectProperties uploadProjectProperties;

    public Upload getFileUpload(MultipartFile multipartFile) {
        Upload upload = UploadUtil.getFileUpload(multipartFile, uploadProjectProperties.getFilePath(), uploadProjectProperties.getStaticPath());
        return upload;
    }

    public Upload uploadFile(MultipartFile multipartFile) {
        Upload upload = UploadUtil.getFileUpload(multipartFile, uploadProjectProperties.getFilePath(), uploadProjectProperties.getStaticPath());
        return uploadFile(multipartFile, upload);
    }

    public Upload uploadFile(MultipartFile multipartFile, Upload upload) {
        upload = UploadUtil.saveFileAndDigester(multipartFile, upload);
        return upload;
    }

    public Upload downloadFile(Upload upload, HttpServletResponse response) {
        UploadUtil.downloadFile(upload, response);
        return upload;
    }


}
