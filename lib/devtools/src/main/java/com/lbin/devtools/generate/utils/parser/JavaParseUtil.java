package com.lbin.devtools.generate.utils.parser;

import com.lbin.devtools.generate.domain.Generate;
import com.lbin.devtools.generate.enums.ModuleType;
import com.lbin.devtools.generate.enums.TierType;
import com.lbin.devtools.generate.utils.CodeUtil;
import com.lbin.devtools.generate.utils.jAngel.JAngel;
import com.lbin.devtools.generate.utils.jAngel.nodes.ClassNode;
import com.lbin.devtools.generate.utils.jAngel.nodes.Document;
import com.lbin.devtools.generate.utils.jAngel.parser.Expression;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 代码生成：java解析工具
 *
 * @author
 * @date 2019/4/4
 */
public class JavaParseUtil {

    /**
     * 获取jAngel文档对象
     *
     * @param generate 生成数据对象
     * @param type     业务模块类型
     */
    public static Document document(Generate generate, TierType type) {
        return document(null, null, generate, type);
    }

    /**
     * 获取jAngel文档对象
     *
     * @param path       文件路径
     * @param expression 模板表达式
     * @param generate   生成数据对象
     * @param type       业务模块类型
     */
    public static Document document(String path, Expression expression, Generate generate, TierType type) {
        // 获取jAngel文档对象
        Document document = null;
        String packageName = getPackage(generate, type);
        if (path != null) {
            document = JAngel.parse(path, expression);
        } else {
            int last = packageName.lastIndexOf('.') + 1;
            document = JAngel.create(packageName.substring(last, packageName.length()));
        }
        document.setPackageName(packageName.substring(0, packageName.lastIndexOf('.')));
        ClassNode clazz = document.getClazz();

        // 作者信息
        String author = generate.getBasic().getAuthor();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String comments = "/**" + CodeUtil.lineBreak +
                " * @author " + author + CodeUtil.lineBreak +
                " * @date " + df.format(new Date()) + CodeUtil.lineBreak +
                " */";
        clazz.setComments(comments);

        return document;
    }

    /**
     * 生成包名路径
     *
     * @param generate 生成数据对象
     * @param type     业务模块类型
     */
    public static String getPackage(Generate generate, TierType type) {
        String packagePath = generate.getBasic().getPackagePath();
        String module = generate.getBasic().getGenModule();
        String entity = generate.getBasic().getTableEntity();


        String modules = "." + CodeUtil.CUSTOM + ".";


        Integer moduleType = generate.getBasic().getModuleType();
        switch (moduleType) {
            case 1:
                modules="." + CodeUtil.CUSTOM + ".";
                break;
            case 2:
                modules="." + CodeUtil.SYSTEM + ".";
                break;
            case 3:
                modules="." + CodeUtil.MODEL + ".";
                break;
        }
        if (generate.getBasic().getGenGroup() != null) {
            module += "." + generate.getBasic().getGenGroup();
        }
        switch (type) {
            case DOMAIN:
                return packagePath + modules + module + ".domain." + entity;
            case DAO:
                return packagePath + modules + module + ".repository." + entity + "Repository";
            case SERVICE:
                return packagePath + modules + module + ".service." + entity + "Service";
            case SERVICE_IMPL:
                return packagePath + modules + module + ".service.impl." + entity + "ServiceImpl";
            case CONTROLLER:
                return packagePath + modules + module + ".controller." + entity + "Controller";
            case MODEL_CONTROLLER:
                return packagePath + modules + module + ".controller." + entity + "ModelController";
            case SERVER:
                return packagePath + modules + module + ".server." + entity + "Server";
            case VALID:
                return packagePath + modules + module + ".validator." + entity + "Valid";
            default:
        }
        return "";
    }

}
