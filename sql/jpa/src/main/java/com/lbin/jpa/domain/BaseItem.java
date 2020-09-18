package com.lbin.jpa.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BaseItem {
    private Long id;
    private List<BaseField> baseFieldList;

    public BaseItem() {
    }

    public BaseItem(Long id, List<BaseField> baseFieldList) {
        this.id = id;
        this.baseFieldList = baseFieldList;
    }
}
