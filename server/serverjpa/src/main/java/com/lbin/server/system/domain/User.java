package com.lbin.server.system.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lbin.common.annotation.BaseClassModel;
import com.lbin.common.annotation.BaseModel;
import com.lbin.sql.jpa.enums.StatusEnum;
import com.lbin.sql.jpa.utils.StatusUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author
 * @date 2018/8/14
 */
@Data
@Entity
@Table(name = "sys_user")
@ToString(exclude = {"roles"})
@EqualsAndHashCode(exclude = {"roles"})
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "update sys_user" + StatusUtil.SLICE_DELETE)
@Where(clause = StatusUtil.NOT_DELETE)
@BaseClassModel(value = "用户")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @BaseModel("用户名")
    private String username;
    @JsonIgnore
    @BaseModel(ignores = true,detail = false)
    private String password;
    @JsonIgnore
    @BaseModel(ignores = true,detail = false)
    private String salt;
    @BaseModel("昵称")
    private String nickname;
    @BaseModel(value = "性别", label = "USER_SEX")
    private Byte sex;
    @BaseModel("手机号码")
    private String phone;
    @BaseModel("电子邮箱")
    private String email;
    @CreatedDate
    @BaseModel("创建时间")
    private Date createDate;
    @LastModifiedDate
    @BaseModel("更新时间")
    private Date updateDate;
    @BaseModel("备注")
    private String remark;
    @BaseModel(value = "状态", label = "DATA_STATUS")
    private Byte status = StatusEnum.OK.getCode();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sys_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonIgnore
    private Set<Role> roles = new HashSet<>(0);

}
