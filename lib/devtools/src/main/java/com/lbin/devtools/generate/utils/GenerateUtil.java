package com.lbin.devtools.generate.utils;

import com.lbin.devtools.generate.domain.Generate;
import com.lbin.devtools.generate.enums.ModuleType;
import com.lbin.devtools.generate.enums.TierType;

import com.lbin.devtools.generate.utils.jAngel.nodes.Document;
import com.lbin.devtools.generate.utils.parser.JavaParseUtil;


import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.util.Arrays;


/**
 * @author
 * @date 2018/10/28
 */
public class GenerateUtil {

    private static final String JAVA_SUFFIX = ".java";
    private static final String HTML_SUFFIX = ".html";
    private static final TierType[] MODULE_TIER = {TierType.DOMAIN, TierType.DAO, TierType.SERVICE, TierType.SERVICE_IMPL};


    /**
     * 生成源码java文件全路径
     *
     * @param generate 生成对象
     * @param tierType 业务模块类型
     */
    public static String getJavaFilePath(Generate generate, TierType tierType) {
        String projectPath = generate.getBasic().getProjectPath();
        String module = generate.getBasic().getGenModule();
        Integer moduleType = generate.getBasic().getModuleType();
        // 获取类包路径
        String packageName = JavaParseUtil.getPackage(generate, tierType);
        String javaPath = packageName.replace(".", "/") + JAVA_SUFFIX;
        String mavenModule = CodeUtil.MODULES_PATH + "/" + module;

        if (moduleType == ModuleType.CUSTOM.getCode() || moduleType == ModuleType.SYSTEM.getCode()) {
            mavenModule = CodeUtil.MODULES_PATH + "/" + CodeUtil.CUSTOM;
        } else if (moduleType == ModuleType.MODEL.getCode()) {
            mavenModule = CodeUtil.MODULES_PATH + "/" + CodeUtil.MODEL;
        }

        return projectPath + mavenModule + CodeUtil.MAVEN_SOURCE_PATH + "/java/" + javaPath;
    }

    /**
     * 生成源码html文件全路径
     *
     * @param generate 生成对象
     * @param tierType 业务模块类型
     */
    public static String getHtmlFilePath(Generate generate, TierType tierType) {
        String projectPath = generate.getBasic().getProjectPath();
        String requestMapping = generate.getBasic().getRequestMapping();
        return projectPath +
                CodeUtil.MODULES_PATH + "/" +
                CodeUtil.CUSTOM +
                CodeUtil.MAVEN_SOURCE_PATH +
                "/resources/templates" +
                requestMapping + "/" +
                tierType.name().toLowerCase() +
                HTML_SUFFIX;
    }

    /**
     * 文件存在异常
     *
     * @param filePath 文件路径
     */
    public static String fileExist(String filePath) {
        return "exist:" + filePath;
    }

    /**
     * 生成源码文件
     *
     * @param filePath 文件路径
     * @param content  文件内容
     */
    public static void generateFile(String filePath, String content) throws FileAlreadyExistsException {
        File file = new File(filePath);
        if (file.exists()) {
            throw new FileAlreadyExistsException(filePath + "文件已经存在");
        } else {
            FileUtil.saveWriter(file, content);
        }
    }
}
