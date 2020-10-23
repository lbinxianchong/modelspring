package com.lbin.model.domain;


import com.lbin.common.annotation.BaseClassModel;
import com.lbin.common.annotation.BaseModel;
import com.lbin.common.enums.BaseTypeEnum;
import com.lbin.jpa.enums.StatusEnum;
import com.lbin.jpa.utils.StatusUtil;
import com.lbin.system.domain.FileUpload;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;


/**
 * @author lbin
 * @date 2020/03/02
 */
@Data
@Entity//实体类（表名）
@ToString//ToString设置
@Table(name = "web_example_model")//（表名）catalog 和 schema 用于设置表所属的数据库目录或模式，通常为数据库名
@EntityListeners(AuditingEntityListener.class)//实体类监听器 例子用于时间
//被@Prepersist注解的方法 ，完成save之前的操作。
//被@Preupdate注解的方法 ，完成update之前的操作。
//被@Postpersist注解的方法 ，完成save之后的操作。
//被@Postupdate注解的方法 ，完成update之后的操作。
@Where(clause = StatusUtil.NOT_DELETE)//查询条件设置
@BaseClassModel(value = "例子", search = true, index = true)//模板自定义
public class ExampleModel implements Serializable {
    // 主键ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @BaseModel(index = true, add = false)
    private Long id;
    // 数据源
    @BaseModel(index = true)//模板自定义
    private Long crawlerid;
    // Url
    @BaseModel(index = true)
    private String baseUrl;
    // 序数
    @BaseModel
    private Integer indexno;
    // 名字
    @BaseModel(value = "名字", search = true, index = true)
    private String name;
    // 文件名字
    private String filename;

    @BaseModel(type = BaseTypeEnum.File, key = "name")
    @OneToOne
    @JoinColumn(name = "fileUpload_id")
    private FileUpload fileUpload;
    // 路径
    private String dir;
    // 进度
    private Integer progress;//0未下载1下载前错误2下载中3下载后错误4下载完成
    // 数据状态
    @BaseModel(add = false)
    private Byte status = StatusEnum.OK.getCode();


}