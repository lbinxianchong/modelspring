package com.lbin.jpa.domain;


import com.lbin.jpa.annotation.BaseClassModel;
import com.lbin.jpa.annotation.BaseModel;

import com.lbin.common.util.ReflectUtil;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BaseFieldModel {
    private String name;
    private List<BaseField> searchList;
    private List<BaseField> indexList;
    private List<BaseField> addList;
    private List<BaseField> detailList;

    public BaseFieldModel() {
    }

    public BaseFieldModel(String name) {
        this.name = name;
    }

    public BaseFieldModel(Class<?> entity) {
        BaseClassModel entityBaseModel = entity.getAnnotation(BaseClassModel.class);
        if (entityBaseModel != null) {
            String entityValue = entityBaseModel.value();
            if (entityValue.trim().length() == 0) {
                setName(entity.getName());
            } else {
                setName(entityValue);
            }
            List<BaseField> searchList = new ArrayList<>();
            List<BaseField> indexList = new ArrayList<>();
            List<BaseField> addList = new ArrayList<>();
            List<BaseField> detailList = new ArrayList<>();
            Field[] fields = entity.getDeclaredFields();
            for (Field field : fields) {
                Class<?> declaringClass = field.getType();
                if (declaringClass.equals(List.class)) {
                    continue;
                }
                boolean add = entityBaseModel.model();
                boolean detail = entityBaseModel.model();
                BaseField baseField = new BaseField();
                baseField.setField(field);
                baseField.setName(field.getName());
                baseField.setTitle(field.getName());
                if (field.isAnnotationPresent(BaseModel.class)) {
                    BaseModel annotation = field.getAnnotation(BaseModel.class);
                    String value = annotation.value();
                    if (value.trim().length() != 0) {
                        baseField.setTitle(value);
                    }
                    if (annotation.search()) {
                        searchList.add(baseField);
                    }
                    if (annotation.index()) {
                        indexList.add(baseField);
                    }
                    add = annotation.add();
                    detail = annotation.detail();
                }
                if (add) {
                    addList.add(baseField);
                }
                if (detail) {
                    detailList.add(baseField);
                }
            }
            if (entityBaseModel.search()) {
                setSearchList(searchList);
            }
            if (entityBaseModel.index()) {
                setIndexList(indexList);
            }
            if (entityBaseModel.add()) {
                setAddList(addList);
            }
            if (entityBaseModel.detail()) {
                setDetailList(detailList);
            }
        } else {
            setName(entity.getName());
        }
    }

    public List<BaseField> getIndexList(Object o) {
        return getFieldValue(indexList, o);
    }

    public List<BaseField> getAddList(Object o) {
        return getFieldValue(addList, o);
    }

    public List<BaseField> getDetailList(Object o) {
        return getFieldValue(detailList, o);
    }

    public BaseItem getIndexListID(Object o) {
        return getBaseItem(indexList, o);
    }


    public BaseItem getAddListID(Object o) {
        return getBaseItem(addList, o);
    }

    public BaseItem getDetailListID(Object o) {
        return getBaseItem(detailList, o);
    }

    public BaseItem getAddListID() {
        return new BaseItem(null,addList);
    }

    public <T> List<List<BaseField>> getIndexList(List<T> list) {
        List<List<BaseField>> listList = new ArrayList<>();
        for (T t : list) {
            List<BaseField> fieldValue = getIndexList(t);
            listList.add(fieldValue);
        }
        return listList;
    }

    public <T> List<BaseItem> getIndexListID(List<T> list) {
        List<BaseItem> listList = new ArrayList<>();
        for (T t : list) {
            BaseItem baseItem = getIndexListID(t);
            listList.add(baseItem);
        }
        return listList;
    }

    private List<BaseField> getFieldValue(List<BaseField> baseFields, Object o) {
        List<BaseField> list = new ArrayList<>();
        for (BaseField b : baseFields) {
            BaseField baseField = new BaseField(b);
            Field field = baseField.getField();
            Object result = ReflectUtil.getFieldValue(o, field);
            baseField.setValue(result);
            list.add(baseField);
        }
        return list;
    }

    private BaseItem getBaseItem(List<BaseField> baseFields, Object o) {
        Long id = (Long) ReflectUtil.getFieldValue(o, "id");
        List<BaseField> fieldValue = getFieldValue(baseFields, o);
        BaseItem baseItem = new BaseItem(id, fieldValue);
        return baseItem;
    }
}
