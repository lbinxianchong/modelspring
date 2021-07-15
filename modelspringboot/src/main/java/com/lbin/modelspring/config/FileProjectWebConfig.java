package com.lbin.modelspring.config;

import cn.hutool.core.util.StrUtil;
import com.lbin.common.properties.FileProjectProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * ���ط���
 */
@Getter
@Setter
@Configuration
public class FileProjectWebConfig implements WebMvcConfigurer {

    @Autowired
    private FileProjectProperties fileProjectProperties;

    //������Դ����
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String staticPath = fileProjectProperties.getStaticPath();
        List<String> staticPathlist = StrUtil.splitTrim(staticPath, ",");
        for (String s : staticPathlist) {
            s = StrUtil.addSuffixIfNot(s, "/**");
            ResourceHandlerRegistration resourceHandlerRegistration = registry.addResourceHandler(s);
            String assetPath = fileProjectProperties.getAssetPath();
            List<String> assetPathlist = StrUtil.splitTrim(assetPath, ",");
            for (String asset : assetPathlist) {
                resourceHandlerRegistration.addResourceLocations(asset);
            }
        }
    }

    @Override
    //��д�����ṩ�Ŀ���������Ľӿ�
    public void addCorsMappings(CorsRegistry registry) {
        //���ӳ��·��
        registry.addMapping("/api/**")
                //������Щԭʼ��
                .allowedOrigins("*")
                //�Ƿ���Cookie��Ϣ
                .allowCredentials(true)
                //������Щԭʼ��(����ʽ)
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                //������Щԭʼ��(ͷ����Ϣ)
                .allowedHeaders("*")
                //��¶��Щͷ����Ϣ����Ϊ�������Ĭ�ϲ��ܻ�ȡȫ��ͷ����Ϣ��
                //.exposedHeaders("Header1", "Header2")
        ;
    }

    /**
     * �ļ�ʵ��·��תΪ������url·��
     *
     * @param absolutePath
     * @return
     */
    public String toServerPath(String absolutePath) {
        absolutePath = absolutePath.replaceAll("\\\\", "/");
        absolutePath = StrUtil.removePrefix(absolutePath, fileProjectProperties.getFilePath());
        absolutePath = StrUtil.removePrefix(absolutePath, fileProjectProperties.getSdkFilePath());
        String staticPath = fileProjectProperties.getStaticPath();
        List<String> staticPathlist = StrUtil.splitTrim(staticPath, ",");
        String s = StrUtil.addPrefixIfNot(absolutePath, staticPathlist.get(0));
        return s;
    }


}
