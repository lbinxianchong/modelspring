package com.lbin.jpa.domain;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;

@Getter
@Setter
public class BaseField {
    private String title;
    private String name;
    private Object value;
    private Field field;

    public BaseField() {
    }

    public BaseField(BaseField baseField) {
        this.title = baseField.title;
        this.name = baseField.name;
        this.value = baseField.value;
        this.field = baseField.field;
    }

}
