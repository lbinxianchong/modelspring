package com.lbin.modelspring.config;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 本地访问
 */
@Configuration
public class FileServerConfig implements WebMvcConfigurer {
    @Value("${fileserver.local.dir}")
    private String localFileServerDir;

    @Value("${fileserver.path}")
    private String localFileServerPath;

    @Value("${fileserver.asset}")
    private String assetFileServerPath;

    @Value("${fileserver.ftp.url}")
    private String ftpFileServerPath;

//    @Value("${web.config.aria2path}")
    private String webConfigAria2path;

    @Value("${web.config.asset}")
    private String webConfigAsset;


    //访问图片方法
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //查询os系统
        //String name = SystemUtil.getOsInfo().getName();
        ResourceHandlerRegistration resourceHandlerRegistration = registry.addResourceHandler("/" + this.getLocalFileServerPath() + "/**");
        if (getAssetFileServerPath().equals("ftp")) {
            resourceHandlerRegistration
                    .addResourceLocations(this.getFtpFileServerPath());
        } else if (getAssetFileServerPath().equals("local")) {
            resourceHandlerRegistration
                    .addResourceLocations("file:" + StrUtil.addSuffixIfNot(this.getLocalFileServerDir(), "/"));
        } else if (getAssetFileServerPath().equals("classpath")) {
            //项目路径资源无需设置
            resourceHandlerRegistration
                    .addResourceLocations("classpath:/format/asset/");
        } else {
            //无需设置
            resourceHandlerRegistration
                    .addResourceLocations("file:" + this.getLocalFileServerDir() + "/")
                    .addResourceLocations("classpath:/format/asset/")
                    .addResourceLocations(this.getFtpFileServerPath());
        }
        String asset = this.getWebConfigAsset();
        List<String> list = StrUtil.splitTrim(asset, ",");
        for (String s : list) {
            resourceHandlerRegistration.addResourceLocations(s);
        }

        // 本地文件夹要以"flie:" 开头，文件夹要以"/" 结束，example：
        //registry.addResourceHandler("/abc/**").addResourceLocations("file:D:/pdf/");
//        super.addResourceHandlers(registry);
    }

    @Override
    //重写父类提供的跨域请求处理的接口
    public void addCorsMappings(CorsRegistry registry) {
        //添加映射路径
        registry.addMapping("/api/**")
                //放行哪些原始域
                .allowedOrigins("*")
                //是否发送Cookie信息
                .allowCredentials(true)
                //放行哪些原始域(请求方式)
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                //放行哪些原始域(头部信息)
                .allowedHeaders("*")
        //暴露哪些头部信息（因为跨域访问默认不能获取全部头部信息）
//                .exposedHeaders("Header1", "Header2")
        ;
    }

    /**
     * 文件实际路径转为服务器url路径
     *
     * @param absolutePath
     * @return
     */
    public String toServerPath(String absolutePath) {
        absolutePath = absolutePath.replaceAll("\\\\", "/");
        return "/" + absolutePath.replace(localFileServerDir, localFileServerPath);
    }

    public String getLocalFileServerDir() {
        return localFileServerDir;
    }

    public void setLocalFileServerDir(String localFileServerDir) {
        this.localFileServerDir = localFileServerDir;
    }

    public String getLocalFileServerPath() {
        return localFileServerPath;
    }

    public void setLocalFileServerPath(String localFileServerPath) {
        this.localFileServerPath = localFileServerPath;
    }

    public String getAssetFileServerPath() {
        return assetFileServerPath;
    }

    public void setAssetFileServerPath(String assetFileServerPath) {
        this.assetFileServerPath = assetFileServerPath;
    }

    public String getFtpFileServerPath() {
        return ftpFileServerPath;
    }

    public void setFtpFileServerPath(String ftpFileServerPath) {
        this.ftpFileServerPath = ftpFileServerPath;
    }

    public String getWebConfigAria2path() {
        return webConfigAria2path;
    }

    public void setWebConfigAria2path(String webConfigAria2path) {
        this.webConfigAria2path = webConfigAria2path;
    }

    public String getWebConfigAsset() {
        return webConfigAsset;
    }

    public void setWebConfigAsset(String webConfigAsset) {
        this.webConfigAsset = webConfigAsset;
    }


}
