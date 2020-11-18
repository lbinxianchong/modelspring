package com.lbin.devtools.generate.template;


import cn.hutool.core.util.StrUtil;
import com.lbin.common.vo.ResultVo;
import com.lbin.devtools.generate.domain.Generate;
import com.lbin.devtools.generate.enums.FieldQuery;
import com.lbin.devtools.generate.enums.TierType;

import com.lbin.devtools.generate.utils.FileUtil;
import com.lbin.devtools.generate.utils.GenerateUtil;
import com.lbin.devtools.generate.utils.jAngel.JAngelContainer;
import com.lbin.devtools.generate.utils.jAngel.nodes.Document;
import com.lbin.devtools.generate.utils.jAngel.parser.Expression;
import com.lbin.devtools.generate.utils.parser.JavaParseUtil;

import java.nio.file.FileAlreadyExistsException;
import java.util.Set;

/**
 * @author
 * @date 2018/10/24
 */
public class ControllerTemplate {

    /**
     * 生成需要导入的包
     */
    public static Set<String> genImports(Generate generate) {
        JAngelContainer container = new JAngelContainer();
        container.importClass(JavaParseUtil.getPackage(generate, TierType.DOMAIN));
        container.importClass(JavaParseUtil.getPackage(generate, TierType.SERVER));
        container.importClass(JavaParseUtil.getPackage(generate, TierType.VALID));
        container.importClass(ResultVo.class);
        return container.getImports();
    }

    /**
     * 生成类体
     */
    private static Document genClazzBody(Generate generate) {
        // 构建数据-模板表达式
        Expression expression = new Expression();
        expression.label("name", StrUtil.lowerFirst(generate.getBasic().getTableEntity()));
        expression.label("entity", generate.getBasic().getTableEntity());
        String path = FileUtil.templatePath(ControllerTemplate.class);

        // 获取jAngel文档对象
        Document document = JavaParseUtil.document(path, expression, generate, TierType.CONTROLLER);
        document.getContainer().importClass(genImports(generate));

        return document;
    }

    /**
     * 生成控制器模板
     */
    public static String generate(Generate generate) {
        // 生成文件
        String filePath = GenerateUtil.getJavaFilePath(generate, TierType.CONTROLLER);
        try {
            Document document = genClazzBody(generate);
            GenerateUtil.generateFile(filePath, document.content());
        } catch (FileAlreadyExistsException e) {
            return GenerateUtil.fileExist(filePath);
        }
        return filePath;
    }
}
