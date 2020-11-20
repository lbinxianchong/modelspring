package com.lbin.server.system.validator;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author
 * @date 2018/8/14
 */
@Data
public class PermissionValid implements Serializable {
    @NotEmpty(message = "标题不能为空")
    private String title;
    @NotEmpty(message = "url地址不能直接为空，可以输入#代替！")
    private String url;
    @NotEmpty(message = "名称不能为空")
    private String name;

}
