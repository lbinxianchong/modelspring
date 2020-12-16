package com.lbin.system.validator;

import lombok.Data;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;

/**
 * @author mo
 * @date 2020/12/11
 */
@Data
public class NavigationValid implements Serializable {
    @NotEmpty(message = "名称不能为空")
    private String name;
}