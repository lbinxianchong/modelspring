package com.lbin.component.fileUpload.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 项目-文件上传配置项
 *
 * @author
 * @date 2018/11/6
 */
@Data
@Component
@ConfigurationProperties(prefix = "project.file")
public class UploadProjectProperties {

    /**
     * 上传文件路径
     */
    private String filePath = "/data";

    /**
     * 上传文件临时路径
     */
    private String uploadPath = "/upload";

    /**
     * 上传文件静态访问路径
     */
    private String staticPath = "/asset";


}
