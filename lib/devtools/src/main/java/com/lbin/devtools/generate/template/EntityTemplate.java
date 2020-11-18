package com.lbin.devtools.generate.template;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.lbin.common.annotation.BaseClassModel;
import com.lbin.common.annotation.BaseIgnoresModel;
import com.lbin.common.annotation.BaseModel;
import com.lbin.common.util.EnumUtil;

import com.lbin.devtools.generate.domain.Generate;
import com.lbin.devtools.generate.enums.FieldType;
import com.lbin.devtools.generate.enums.TierType;
import com.lbin.devtools.generate.utils.GenerateUtil;
import com.lbin.devtools.generate.utils.jAngel.nodes.*;
import com.lbin.devtools.generate.utils.parser.JavaParseUtil;
import com.lbin.server.system.domain.User;
import com.lbin.sql.jpa.enums.StatusEnum;
import com.lbin.sql.jpa.utils.StatusUtil;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
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
    public static Document genClazzBody(Generate generate) {
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
        clazz.addAnnotation(EntityListeners.class, Format.of("$T.class", AuditingEntityListener.class));
        clazz.addAnnotation(Where.class, Format.of("clause = $T.NOT_DELETE", StatusUtil.class));

        clazz.addAnnotation(BaseClassModel.class, Format.of("value = $S", generate.getBasic().getGenTitle()));
        clazz.addAnnotation(BaseIgnoresModel.class);

        // 生成类字段
        generate.getFields().forEach(field -> {
            // 创建字段节点
            String name = field.getName();
            FieldNode node = new FieldNode(name);


            String title = field.getTitle();
            boolean query = field.getQuery() != null && field.getQuery() > 0;
            Format format = Format.of("value=$S,search=$O,index=$O", title,query,field.isShow());
            node.addAnnotation(BaseModel.class,format);

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
                    node.setType(User.class);
                    break;
                case "updateBy":
                    node.addAnnotation(LastModifiedBy.class);
                    node.addAnnotation(ManyToOne.class, Format.of("fetch=$T.LAZY", FetchType.class));
                    node.addAnnotation(NotFound.class, Format.of("action=$T.IGNORE", NotFoundAction.class));
                    node.addAnnotation(JoinColumn.class, Format.of("name=$S", "update_by"));
                    node.addAnnotation(JsonIgnore.class);
                    node.setType(User.class);
                    break;
                case "status":
                    node.setValue(Format.of("$T.OK.getCode()", StatusEnum.class));
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
