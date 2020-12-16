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

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 * @author mo
 * @date 2020/12/11
 */
@Entity
@Table(name = "sys_menu")
@EntityListeners(AuditingEntityListener.class)
@Where(clause = StatusUtil.NOT_DELETE)
@Data
@ToString(exclude = {"createBy", "updateBy"})
@EqualsAndHashCode(exclude = {"createBy", "updateBy"})
@BaseClassModel(value = "菜单")
@BaseIgnoresModel
public class Menu implements Serializable {

    // 主键ID
    @BaseModel(value = "主键ID", index = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 菜单名称
    @BaseModel(value = "名称", search = true, index = true)
    private String name;
    // URL地址
    @BaseModel(value = "URL地址", index = true)
    private String url;
    // 排序
    @BaseModel(value = "排序", index = true)
    private Integer sort;
    // 备注
    @BaseModel(value = "备注")
    private String remark;
    // 创建时间
    @BaseModel(value = "创建时间", index = true)
    @CreatedDate
    private Date createDate;
    // 更新时间
    @BaseModel(value = "更新时间", index = true)
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
    @BaseModel(value = "数据状态", index = true)
    private Byte status = StatusEnum.OK.getCode();

    @BaseModel(value = "权限", key = "name")
    @OneToOne
    @JoinColumn(name = "permission_id")
    private Permission permission;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    @JsonIgnore
    @JoinColumn(name = "navigation_id")
    @BaseModel(value = "导航",key = "name")
    private Navigation navigation;


}