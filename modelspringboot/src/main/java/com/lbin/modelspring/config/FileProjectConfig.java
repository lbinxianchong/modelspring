package com.lbin.modelspring.config;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;
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
@Getter
@Setter
@Configuration
public class FileProjectConfig implements WebMvcConfigurer {

    @Value("${project.file.staticPath}")
    private String staticPath;

    @Value("${project.file.filePath}")
    private String filePath;

    @Value("${project.file.sdkFilePath}")
    private String sdkFilePath;

    @Value("${project.file.assetPath}")
    private String assetPath;

    //访问图片方法
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String staticPath = getStaticPath();
        List<String> staticPathlist = StrUtil.splitTrim(staticPath, ",");
        for (String s : staticPathlist) {
            s = StrUtil.addSuffixIfNot(s, "/**");
            ResourceHandlerRegistration resourceHandlerRegistration = registry.addResourceHandler(s);
            String assetPath = getAssetPath();
            List<String> assetPathlist = StrUtil.splitTrim(assetPath, ",");
            for (String asset : assetPathlist) {
                resourceHandlerRegistration.addResourceLocations(asset);
            }
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
        absolutePath = StrUtil.removePrefix(absolutePath, getFilePath());
        absolutePath = StrUtil.removePrefix(absolutePath, getSdkFilePath());
        String staticPath = getStaticPath();
        List<String> staticPathlist = StrUtil.splitTrim(staticPath, ",");
        String s = StrUtil.addPrefixIfNot(absolutePath, staticPathlist.get(0));
        return s;
    }


}
