package com.lbin.server.system.domain;

import com.lbin.common.annotation.BaseClassModel;
import com.lbin.common.annotation.BaseModel;
import com.lbin.sql.jpa.enums.StatusEnum;
import com.lbin.sql.jpa.utils.StatusUtil;
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
 * @author
 * @date 2018/8/14
 */
@Data
@Entity
@ToString//ToString设置
@Table(name = "sys_dict")
@EntityListeners(AuditingEntityListener.class)
@Where(clause = StatusUtil.NOT_DELETE)
@BaseClassModel(value = "上傳文件")
public class Dict implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @BaseModel(index = true, add = false)
    private Long id;
    private String name;
    private String title;
    private Byte type;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String value;
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
    private Byte status = StatusEnum.OK.getCode();
}
