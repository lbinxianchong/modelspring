package com.lbin.common.properties;

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
public class FileProjectProperties {

    /**
     * 上传文件静态访问Url
     */
    private String staticPath = "/asset";

    /**
     * 上传文件路径
     */
    private String filePath = "/format";

    /**
     * 上传文件临时路径
     */
    private String uploadPath = "/upload";

    /**
     * Excel临时路径
     */
    private String excelPath = "/excel";

    /**
     * 第三方插件临时路径
     */
    private String sdkFilePath = "/data";

    /**
     * 访问url文件实际路径
     */
    private String assetPath = "file:/data/,classpath:/format/,http://127.0.0.1:6800";


}
