package com.lbin.system.domain;


import com.lbin.common.annotation.BaseClassModel;
import com.lbin.common.annotation.BaseModel;
import com.lbin.component.fileUpload.data.Upload;
import com.lbin.jpa.enums.StatusEnum;
import com.lbin.jpa.utils.StatusUtil;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * @author lbin
 * @date 2020/03/02
 */
@Data
@Entity//实体类（表名）
@ToString//ToString设置
@Table(name = "sys_file_upload")//（表名）catalog 和 schema 用于设置表所属的数据库目录或模式，通常为数据库名
@EntityListeners(AuditingEntityListener.class)//实体类监听器 例子用于时间
@Where(clause = StatusUtil.NOT_DELETE)//查询条件设置
@BaseClassModel(value = "上傳文件",model = true)
public class FileUpload implements Serializable {
    // 主键ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @BaseModel(index = true, add = false)
    private Long id;

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件路径
     */
    private String path;

    /**
     * url路径
     */
    private String url;

    /**
     * 文件类型
     */
    private String mime;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 文件md5值
     */
    private String md5;

    /**
     * 文件sha1值
     */
    private String sha1;

    /**
     * 文件UUID
     */
    private String uuid;

    // 备注
    private String remark;
    // 创建时间
    @CreatedDate
    @BaseModel(index = true, add = false)
    private Date createDate;
    // 更新时间
    @LastModifiedDate
    @BaseModel(index = true, add = false)
    private Date updateDate;
    // 数据状态
    @BaseModel(add = false)
    private Byte status = StatusEnum.OK.getCode();

    public FileUpload() {
    }


    public FileUpload(Upload upload) {
        this.name = upload.getName();
        this.path = upload.getPath();
        this.url = upload.getUrl();
        this.mime = upload.getMime();
        this.size = upload.getSize();
        this.md5 = upload.getMd5();
        this.sha1 = upload.getSha1();
        this.uuid = upload.getUuid();
    }

    public Upload getUpload() {
        Upload upload = new Upload();
        upload.setName(name);
        upload.setPath(path);
        upload.setUrl(url);
        upload.setMime(mime);
        upload.setSize(size);
        upload.setMd5(md5);
        upload.setSha1(sha1);
        upload.setUuid(uuid);
        return upload;
    }
}