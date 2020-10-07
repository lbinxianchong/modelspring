package com.lbin.common.domain;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;

@Getter
@Setter
public class BaseField {
    private String type;
    private String title;
    private String label;
    private String key;
    private String name;
    private Object value;
    private Field field;


    public BaseField() {
    }

    public BaseField(BaseField baseField) {
        this.type = baseField.type;
        this.title = baseField.title;
        this.label = baseField.label;
        this.key = baseField.key;
        this.name = baseField.name;
        this.value = baseField.value;
        this.field = baseField.field;
    }

}
