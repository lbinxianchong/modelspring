package com.lbin.component.shiro.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class User implements Serializable {

    private String uuid;

    private String username;

    private String password;

    private String salt;

    private String nickname;

}
