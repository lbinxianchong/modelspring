package com.lbin.devtools.generate.template;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.lbin.common.annotation.BaseClassModel;
import com.lbin.common.annotation.BaseIgnoresModel;
import com.lbin.common.annotation.BaseModel;
import com.lbin.common.util.EnumUtil;

import com.lbin.devtools.generate.domain.Generate;
import com.lbin.devtools.generate.enums.FieldType;
import com.lbin.devtools.generate.enums.TierType;
import com.lbin.devtools.generate.utils.FileUtil;
import com.lbin.devtools.generate.utils.GenerateUtil;
import com.lbin.devtools.generate.utils.jAngel.nodes.*;
import com.lbin.devtools.generate.utils.jAngel.parser.Expression;
import com.lbin.devtools.generate.utils.parser.JavaParseUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.file.FileAlreadyExistsException;
import java.util.Date;
import java.util.Map;

/**
 * @author
 * @date 2018/10/23
 */
public class EntityTemplate {

    /**
     * 生成类体
     */
    /*public static Document genClazzBody(Generate generate) {
        // 获取生成信息
        String prefix = generate.getBasic().getTablePrefix();
        String tableName = generate.getBasic().getTableName();


        // 获取jAngel文档对象
        Document document = JavaParseUtil.document(generate, TierType.DOMAIN);
        ClassNode clazz = document.getClazz();

        // 实现接口及注解
        clazz.addImplements(Serializable.class);
        clazz.addAnnotation(Data.class);
        clazz.addAnnotation(Entity.class);
        clazz.addAnnotation(Table.class, Format.of("name=$S", prefix + tableName));
        Format of = Format.of("exclude = {\"createBy\", \"updateBy\"}");
        clazz.addAnnotation(ToString.class, of);
        clazz.addAnnotation(EqualsAndHashCode.class, of);
        clazz.addAnnotation(EntityListeners.class, Format.of("$T.class", AuditingEntityListener.class));

        //com.lbin.sql.jpa.utils.StatusUtil
        clazz.addAnnotation(Where.class, Format.of("clause = StatusUtil.NOT_DELETE"));
        clazz.importClass("org.hibernate.annotations.Where");
        clazz.importClass("com.lbin.sql.jpa.utils.StatusUtil");

        clazz.addAnnotation(BaseClassModel.class, Format.of("value = $S", generate.getBasic().getGenTitle()));
        clazz.addAnnotation(BaseIgnoresModel.class);


        // 生成类字段
        generate.getFields().forEach(field -> {
            // 创建字段节点
            String name = field.getName();
            FieldNode node = new FieldNode(name);


            String title = field.getTitle();
            boolean query = field.getQuery() != null && field.getQuery() > 0;
            Format format = Format.of("value=$S", title);
            if (query && !field.isShow()) {
                format = Format.of("value=$S,search=$O", title, query);
            } else if (!query && field.isShow()) {
                format = Format.of("value=$S,index=$O", title, field.isShow());
            } else if (query && field.isShow()) {
                format = Format.of("value=$S,search=$O,index=$O", title, query, field.isShow());
            }
            node.addAnnotation(BaseModel.class, format);

            // 特殊字段处理
            switch (name) {
                case "id":
                    node.addAnnotation(Id.class);
                    node.addAnnotation(GeneratedValue.class, Format.of("strategy=$T.IDENTITY", GenerationType.class));
                    break;
                case "createDate":
                    node.addAnnotation(CreatedDate.class);
                    node.setType(Date.class);
                    break;
                case "updateDate":
                    node.addAnnotation(LastModifiedDate.class);
                    node.setType(Date.class);
                    break;
                case "createBy":
                    node.addAnnotation(CreatedBy.class);
                    node.addAnnotation(ManyToOne.class, Format.of("fetch=$T.LAZY", FetchType.class));
                    node.addAnnotation(NotFound.class, Format.of("action=$T.IGNORE", NotFoundAction.class));
                    node.addAnnotation(JoinColumn.class, Format.of("name=$S", "create_by"));
                    node.addAnnotation(JsonIgnore.class);
                    //com.lbin.server.system.domain.User
                    node.setValue("User");
                    node.importClass("com.lbin.server.system.domain.User");
                    break;
                case "updateBy":
                    node.addAnnotation(LastModifiedBy.class);
                    node.addAnnotation(ManyToOne.class, Format.of("fetch=$T.LAZY", FetchType.class));
                    node.addAnnotation(NotFound.class, Format.of("action=$T.IGNORE", NotFoundAction.class));
                    node.addAnnotation(JoinColumn.class, Format.of("name=$S", "update_by"));
                    node.addAnnotation(JsonIgnore.class);
                    //com.lbin.server.system.domain.User
                    node.setValue("User");
                    node.importClass("com.lbin.server.system.domain.User");
                    break;
                case "status":
                    //com.lbin.sql.jpa.enums.StatusEnum
                    node.setValue(Format.of("StatusEnum.OK.getCode()"));
                    node.importClass("com.lbin.sql.jpa.enums.StatusEnum");
                    break;
                default:
            }

            // 获取字段类型
            Map<Integer, String> fieldType = EnumUtil.enumToMap(FieldType.class);
            String type = fieldType.get(field.getType());

            // 判断如果类型为Text则进行注解处理
            if (type.equals(FieldType.Text.getMessage())) {
                node.addAnnotation(Lob.class);
                node.addAnnotation(Column.class, Format.of("columnDefinition=$S", "TEXT"));
                node.setType(String.class);
            }

            // 将字段节点附加到实体类上
            if (node.getType() == null) {
                node.setType(type);
                // 格式化时间类型
                if (type.equals(FieldType.Date.getMessage())) {
                    node.addAnnotation(DateTimeFormat.class, Format.of("pattern=$S", "yyyy-MM-dd HH:mm:ss"));
                }
                // 处理高精度浮点型BigDecimal
                if (type.equals(FieldType.BigDecimal.getMessage())) {
                    document.getContainer().importClass(BigDecimal.class);
                }
            }

            // 排除主键ID注释
            if (!"name".equals(name)) {
                node.setComments("// " + field.getTitle());
            }

            node.accessSym(Modifier.PRIVATE);
            clazz.append(node);
        });

        return document;
    }*/
    public static Document genClazzBody(Generate generate) {
        // 获取生成信息

        Expression expression = new Expression();
        expression.label("name", StrUtil.lowerFirst(generate.getBasic().getTableEntity()));
        expression.label("entity", generate.getBasic().getTableEntity());
        expression.label("prefix", generate.getBasic().getTablePrefix());
        expression.label("tableName", generate.getBasic().getTableName());
        expression.label("title", generate.getBasic().getGenTitle());
        String path = FileUtil.templatePath(EntityTemplate.class);
        // 获取jAngel文档对象
        Document document = JavaParseUtil.document(path,expression,generate, TierType.DOMAIN);

        ClassNode clazz = document.getClazz();

        // 实现接口及注解
        clazz.addImplements(Serializable.class);
        clazz.addAnnotation(Data.class);
        Format of = Format.of("exclude = {\"createBy\", \"updateBy\"}");
        clazz.addAnnotation(ToString.class, of);
        clazz.addAnnotation(EqualsAndHashCode.class, of);

        clazz.addAnnotation(BaseClassModel.class, Format.of("value = $S", generate.getBasic().getGenTitle()));
        clazz.addAnnotation(BaseIgnoresModel.class);


        // 生成类字段
        generate.getFields().forEach(field -> {
            // 创建字段节点
            String name = field.getName();
            FieldNode node = new FieldNode(name);


            String title = field.getTitle();
            boolean query = field.getQuery() != null && field.getQuery() > 0;
            Format format = Format.of("value=$S", title);
            if (query && !field.isShow()) {
                format = Format.of("value=$S,search=$O", title, query);
            } else if (!query && field.isShow()) {
                format = Format.of("value=$S,index=$O", title, field.isShow());
            } else if (query && field.isShow()) {
                format = Format.of("value=$S,search=$O,index=$O", title, query, field.isShow());
            }
            node.addAnnotation(BaseModel.class, format);

            // 特殊字段处理
            switch (name) {
                case "id":
                    node.addAnnotation("Id");
                    node.addAnnotation("GeneratedValue(strategy = GenerationType.IDENTITY)");
                    break;
                case "createDate":
                    node.addAnnotation("CreatedDate");
                    node.setType(Date.class);
                    break;
                case "updateDate":
                    node.addAnnotation("LastModifiedDate");
                    node.setType(Date.class);
                    break;
                case "createBy":

                    node.addAnnotation("CreatedBy");
                    node.addAnnotation("ManyToOne(fetch = FetchType.LAZY)");
                    node.addAnnotation("NotFound(action = NotFoundAction.IGNORE)");
                    node.addAnnotation("JoinColumn(name = \"create_by\")");
                    node.addAnnotation(JsonIgnore.class);

                    //com.lbin.server.system.domain.User
                    node.setType("User");
                    node.importClass("com.lbin.server.system.domain.User");
                    break;
                case "updateBy":
                    node.addAnnotation("LastModifiedBy");
                    node.addAnnotation("ManyToOne(fetch = FetchType.LAZY)");
                    node.addAnnotation("NotFound(action = NotFoundAction.IGNORE)");
                    node.addAnnotation("JoinColumn(name = \"update_by\")");
                    node.addAnnotation(JsonIgnore.class);
                    //com.lbin.server.system.domain.User
                    node.setType("User");
                    node.importClass("com.lbin.server.system.domain.User");
                    break;
                case "status":
                    //com.lbin.sql.jpa.enums.StatusEnum
                    node.setValue(Format.of("StatusEnum.OK.getCode()"));
                    node.importClass("com.lbin.sql.jpa.enums.StatusEnum");
                    break;
                default:
            }

            // 获取字段类型
            Map<Integer, String> fieldType = EnumUtil.enumToMap(FieldType.class);
            String type = fieldType.get(field.getType());

            // 判断如果类型为Text则进行注解处理
            if (type.equals(FieldType.Text.getMessage())) {
                node.addAnnotation("@Lob");
                node.addAnnotation("@Column(columnDefinition=TEXT)");
                node.setType(String.class);
            }

            // 将字段节点附加到实体类上
            if (node.getType() == null) {
                node.setType(type);
                // 格式化时间类型
                if (type.equals(FieldType.Date.getMessage())) {
                    node.addAnnotation(DateTimeFormat.class, Format.of("pattern=$S", "yyyy-MM-dd HH:mm:ss"));
                }
                // 处理高精度浮点型BigDecimal
                if (type.equals(FieldType.BigDecimal.getMessage())) {
                    document.getContainer().importClass(BigDecimal.class);
                }
            }

            // 排除主键ID注释
            if (!"name".equals(name)) {
                node.setComments("// " + field.getTitle());
            }

            node.accessSym(Modifier.PRIVATE);
            clazz.append(node);
        });

        return document;
    }

    /**
     * 生成实体类模板
     */
    public static String generate(Generate generate) {
        // 生成文件
        String filePath = GenerateUtil.getJavaFilePath(generate, TierType.DOMAIN);
        try {
            Document document = genClazzBody(generate);
            GenerateUtil.generateFile(filePath, document.content());
        } catch (FileAlreadyExistsException e) {
            return GenerateUtil.fileExist(filePath);
        }
        return filePath;
    }
}
