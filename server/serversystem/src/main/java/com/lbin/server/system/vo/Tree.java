package com.lbin.server.system.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Tree {
    private Long id;
    private String title;
    private String name;
    private String url;
    private List<Tree> children;
}
