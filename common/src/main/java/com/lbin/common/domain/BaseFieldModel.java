package com.lbin.common.domain;


import com.lbin.common.annotation.BaseClassModel;
import com.lbin.common.annotation.BaseModel;

import com.lbin.common.util.CacheUtil;
import com.lbin.common.util.ReflectUtil;
import lombok.Getter;
import lombok.Setter;
import net.sf.ehcache.Cache;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BaseFieldModel {
    private String name;
    private Class entity;
    private List<BaseField> ignoresList;
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
        setEntity(entity);
        setName(entity.getName());
        BaseClassModel entityBaseModel = entity.getAnnotation(BaseClassModel.class);
        if (entityBaseModel != null) {
            String entityValue = entityBaseModel.value();
            if (entityValue.trim().length() > 0) {
                setName(entityValue);
            }
            List<BaseField> ignoresList = new ArrayList<>();
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
                baseField.setType("String");
                if (field.isAnnotationPresent(BaseModel.class)) {
                    BaseModel annotation = field.getAnnotation(BaseModel.class);
                    String type = annotation.type();
                    if (type.trim().length() != 0) {
                        baseField.setType(type);
                    }
                    String value = annotation.value();
                    if (value.trim().length() != 0) {
                        baseField.setTitle(value);
                    }
                    String label = annotation.label();
                    if (label.trim().length() != 0) {
                        baseField.setLabel(label);
                    }
                    String key = annotation.key();
                    if (key.trim().length() != 0) {
                        baseField.setKey(key);
                    }
                    if (annotation.ignores()) {
                        ignoresList.add(baseField);
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
            if (entityBaseModel.ignores()) {
                setIgnoresList(ignoresList);
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
        }
    }

    public List<String> getIgnoresList() {
        List<String> list = new ArrayList<>();
        if (ignoresList == null) {
            ignoresList = new ArrayList<>();
        }
        for (BaseField baseField : ignoresList) {
            list.add(baseField.getName());
        }
        return list;
    }

    public List<BaseField> getIndexList(Object o) {
        return getIndexList(o, true);
    }

    public List<BaseField> getAddList(Object o) {
        return getAddList(o, false);
    }

    public List<BaseField> getDetailList(Object o) {
        return getDetailList(o, true);
    }

    public BaseItem getIndexListID(Object o) {
        return getIndexListID(o, true);
    }

    public BaseItem getAddListID(Object o) {
        return getAddListID(o, false);
    }

    public BaseItem getDetailListID(Object o) {
        return getDetailListID(o, true);
    }

    public List<BaseField> getIndexList(Object o, boolean isKey) {
        return getFieldValue(indexList, o, isKey);
    }

    public List<BaseField> getAddList(Object o, boolean isKey) {
        return getFieldValue(addList, o, isKey);
    }

    public List<BaseField> getDetailList(Object o, boolean isKey) {
        return getFieldValue(detailList, o, isKey);
    }

    public BaseItem getIndexListID(Object o, boolean isKey) {
        return getBaseItem(getIndexList(o, isKey), o);
    }

    public BaseItem getAddListID(Object o, boolean isKey) {
        return getBaseItem(getAddList(o, isKey), o);
    }

    public BaseItem getDetailListID(Object o, boolean isKey) {
        return getBaseItem(getDetailList(o, isKey), o);
    }

    public BaseItem getAddListID() {
        return new BaseItem(null, addList);
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

    public <T> List<List<BaseField>> getAddList(List<T> list) {
        List<List<BaseField>> listList = new ArrayList<>();
        for (T t : list) {
            List<BaseField> fieldValue = getAddList(t);
            listList.add(fieldValue);
        }
        return listList;
    }

    public <T> List<BaseItem> getAddListID(List<T> list) {
        List<BaseItem> listList = new ArrayList<>();
        for (T t : list) {
            BaseItem baseItem = getAddListID(t);
            listList.add(baseItem);
        }
        return listList;
    }

    public <T> List<List<BaseField>> getDetailList(List<T> list) {
        List<List<BaseField>> listList = new ArrayList<>();
        for (T t : list) {
            List<BaseField> fieldValue = getDetailList(t);
            listList.add(fieldValue);
        }
        return listList;
    }

    public <T> List<BaseItem> getDetailListID(List<T> list) {
        List<BaseItem> listList = new ArrayList<>();
        for (T t : list) {
            BaseItem baseItem = getDetailListID(t);
            listList.add(baseItem);
        }
        return listList;
    }

    private List<BaseField> getFieldValue(List<BaseField> baseFields, Object o, boolean isKey) {
        List<BaseField> list = new ArrayList<>();
        for (BaseField b : baseFields) {
            BaseField baseField = new BaseField(b);
            Field field = baseField.getField();
            Object result = ReflectUtil.getFieldValue(o, field);
            Object value = result;
            if (isKey || b.getType().equals("File")) {
                String key = baseField.getKey();
                if (key != null) {
                    String label = baseField.getLabel();
                    if (label != null) {
                        Cache cache = CacheUtil.getCache(label);
                        value = CacheUtil.keyValue(cache, key, result.toString());
                    } else {
                        value = ReflectUtil.getFieldValue(result, key);
                    }
                }
            }
            baseField.setValue(value);
            list.add(baseField);
        }
        return list;
    }


    private BaseItem getBaseItem(List<BaseField> baseFields, Object o) {
        Long id = (Long) ReflectUtil.getFieldValue(o, "id");
        BaseItem baseItem = new BaseItem(id, baseFields);
        return baseItem;
    }
}
