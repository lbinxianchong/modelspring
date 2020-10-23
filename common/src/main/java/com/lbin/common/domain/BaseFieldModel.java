package com.lbin.common.domain;


import cn.hutool.core.util.StrUtil;
import com.lbin.common.annotation.BaseClassModel;
import com.lbin.common.annotation.BaseIgnoresModel;
import com.lbin.common.annotation.BaseModel;

import com.lbin.common.config.FieldsConfigProperties;
import com.lbin.common.util.CacheUtil;
import com.lbin.common.util.ReflectUtil;
import lombok.Getter;
import lombok.Setter;
import net.sf.ehcache.Cache;

import java.lang.reflect.Field;
import java.util.*;


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

    public BaseFieldModel(Class<?> entity) {
        setEntity(entity);
        setName(entity.getName());

        boolean ignores = true;
        boolean search = false;
        boolean index = false;
        boolean add = true;
        boolean detail = true;

        BaseClassModel baseClassModel = entity.getAnnotation(BaseClassModel.class);
        if (baseClassModel != null) {
            String name = baseClassModel.value();
            if (name.trim().length() > 0) {
                setName(name);
            }
            ignores = baseClassModel.ignores();
            search = baseClassModel.search();
            index = baseClassModel.index();
            add = baseClassModel.add();
            detail = baseClassModel.detail();
        }

        List<BaseField> ignoresList = new ArrayList<>();
        List<BaseField> searchList = new ArrayList<>();
        List<BaseField> indexList = new ArrayList<>();
        List<BaseField> addList = new ArrayList<>();
        List<BaseField> detailList = new ArrayList<>();

        BaseIgnoresModel baseIgnoresModel = entity.getAnnotation(BaseIgnoresModel.class);

        String[] ignoresFields = FieldsConfigProperties.ignoresFields;
        String[] searchFields = FieldsConfigProperties.searchFields;
        String[] indexFields = FieldsConfigProperties.indexFields;
        String[] addFields = FieldsConfigProperties.addFields;
        String[] detailFields = FieldsConfigProperties.detailFields;

        if (baseIgnoresModel != null) {
            ignoresFields = baseIgnoresModel.ignores();
            searchFields = baseIgnoresModel.search();
            indexFields = baseIgnoresModel.index();
            addFields = baseIgnoresModel.add();
            detailFields = baseIgnoresModel.detail();
        }

        Field[] fields = entity.getDeclaredFields();
        for (Field field : fields) {

            Class<?> declaringClass = field.getType();
            String name = field.getName();
            if (declaringClass.equals(List.class) || declaringClass.equals(Set.class)) {
                continue;
            }

            BaseField baseField = new BaseField();
            baseField.setField(field);
            baseField.setName(name);
            baseField.setTitle(name);
            baseField.setType("Text");

            boolean ignoresField = ignores(ignores, name, ignoresFields);
            boolean searchField = ignores(search, name, searchFields);
            boolean indexField = ignores(index, name, indexFields);
            boolean addField = ignores(add, name, addFields);
            boolean detailField = ignores(detail, name, detailFields);

            if (field.isAnnotationPresent(BaseModel.class)) {
                BaseModel annotation = field.getAnnotation(BaseModel.class);
                String type = annotation.type().name();
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

                ignoresField = annotation.ignores();
                searchField = annotation.search();
                indexField = annotation.index();
                addField = annotation.add();
                detailField = annotation.detail();
            }


            if (ignoresField) {
                ignoresList.add(baseField);
            }
            if (searchField) {
                searchList.add(baseField);
            }
            if (indexField) {
                indexList.add(baseField);
            }
            if (addField) {
                addList.add(baseField);
            }
            if (detailField) {
                detailList.add(baseField);
            }
        }
        setIgnoresList(ignoresList);
        setSearchList(searchList);
        setIndexList(indexList);
        setAddList(addList);
        setDetailList(detailList);
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

    public List<BaseField> getSearchList(Object o) {
        return getSearchList(o, false);
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

    public List<BaseField> getSearchList(Object o, boolean isKey) {
        return getFieldValue(searchList, o, isKey);
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
            if (b.getType().equals("File")) {
                Map<String, Object> file = new HashMap<>();
                file.put("id", ReflectUtil.getFieldValue(result, "id"));
                file.put("name", ReflectUtil.getFieldValue(result, "name"));
                value = file;
            } else if (isKey) {
                String key = baseField.getKey();
                String label = baseField.getLabel();
                boolean k = key == null;
                boolean l = label == null;
                if (!k && l) {
                    value = ReflectUtil.getFieldValue(result, key);
                } else if (k && !l) {
                    value = CacheUtil.keyValueDict(label, String.valueOf(result));
                } else if (!k && !l) {
                    Cache cache = CacheUtil.getCache(label);
                    value = CacheUtil.keyValue(cache, key, String.valueOf(result));
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

    private boolean ignores(boolean contains, String name, String... field) {
        contains = !StrUtil.containsAnyIgnoreCase(name, field);
        return contains;
    }
}
