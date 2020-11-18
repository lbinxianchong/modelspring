package com.lbin.devtools.generate.utils;

import cn.hutool.core.util.StrUtil;
import com.lbin.devtools.generate.domain.Generate;
import com.lbin.devtools.generate.enums.TierType;
import com.lbin.devtools.generate.template.ServiceTemplate;
import com.lbin.devtools.generate.utils.jAngel.nodes.Document;
import com.lbin.devtools.generate.utils.jAngel.parser.Expression;
import com.lbin.devtools.generate.utils.parser.JavaParseUtil;

import java.nio.file.FileAlreadyExistsException;
import java.util.Set;

public class TemplateUtil {

    /**
     * 生成模板
     */
    public static String generate(Generate generate, TierType tierType, Set<String> imports,Class<?> clazz) {
        Document document = genClazzBody(generate, tierType, imports,clazz);
        return generate(generate,document,tierType);
    }

    /**
     * 生成模板
     */
    public static String generate(Generate generate, Document document, TierType tierType) {
        // 生成文件
        String filePath = GenerateUtil.getJavaFilePath(generate, tierType);
        try {
            GenerateUtil.generateFile(filePath, document.content());
        } catch (FileAlreadyExistsException e) {
            return GenerateUtil.fileExist(filePath);
        }
        return filePath;
    }

    /**
     * 生成类体
     */
    public static Document genClazzBody(Generate generate, TierType tierType, Set<String> imports,Class<?> clazz) {
        // 构建数据-模板表达式
        Expression expression = new Expression();
        expression.label("name", StrUtil.lowerFirst(generate.getBasic().getTableEntity()));
        expression.label("entity", generate.getBasic().getTableEntity());
        expression.label("requestMapping", generate.getBasic().getRequestMapping());

        String path = FileUtil.templatePath(clazz);

        // 获取jAngel文档对象
        Document document = JavaParseUtil.document(path, expression, generate,tierType);
        document.getContainer().importClass(imports);

        return document;
    }
}
