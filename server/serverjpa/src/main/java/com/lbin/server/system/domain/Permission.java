package com.lbin.server.system.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lbin.common.annotation.BaseClassModel;
import com.lbin.common.annotation.BaseIgnoresModel;
import com.lbin.common.annotation.BaseModel;
import com.lbin.sql.jpa.enums.StatusEnum;
import com.lbin.sql.jpa.utils.StatusUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "sys_permission")
@ToString(exclude = {"roles", "createBy", "updateBy"})
@EqualsAndHashCode(exclude = {"roles", "createBy", "updateBy"})
@EntityListeners(AuditingEntityListener.class)
@Where(clause = StatusUtil.NOT_DELETE)
@BaseClassModel(value = "权限")
@BaseIgnoresModel
public class Permission {
    // 主键ID
    @BaseModel(value = "主键ID",  index = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @BaseModel(value = "名称", search = true, index = true)
    private String name;
    // 标题
    @BaseModel(value = "标题", search = true, index = true)
    private String title;
    // url
    @BaseModel(value = "url",  index = true)
    private String url;
    // 级别
    @BaseModel(value = "级别",  index = true)
    private Integer level ;
    // 备注
    @BaseModel(value = "备注")
    private String remark;
    // 创建时间
    @BaseModel(value = "创建时间")
    @CreatedDate
    private Date createDate;
    // 更新时间
    @BaseModel(value = "更新时间")
    @LastModifiedDate
    private Date updateDate;
    // 创建者
    @BaseModel(value = "创建者")
    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "create_by")
    @JsonIgnore
    private User createBy;
    // 更新者
    @BaseModel(value = "更新者")
    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "update_by")
    @JsonIgnore
    private User updateBy;
    // 数据状态
    @BaseModel(value = "数据状态")
    private Byte status = StatusEnum.OK.getCode();

    @ManyToMany(mappedBy = "permissions")
    @JsonIgnore
    private Set<Role> roles = new HashSet<>(0);
}
