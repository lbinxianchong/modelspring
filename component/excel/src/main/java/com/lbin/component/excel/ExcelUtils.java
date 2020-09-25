package com.lbin.component.excel;


import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

import com.lbin.common.domain.BaseField;
import com.lbin.common.domain.BaseItem;


import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * @author
 * @date 2018/11/8
 */
public class ExcelUtils {

    /**
     * 标题
     *
     * @param writer
     * @param titleList
     * @return
     */
    public static ExcelWriter getTitleCommon(ExcelWriter writer, List<BaseField> titleList) {
        writer.addHeaderAlias("id", "id");
        for (BaseField baseField : titleList) {
            String title = baseField.getTitle();
            String name = baseField.getName();
            if (name.equals("id")) {
                continue;
            }
            writer.addHeaderAlias(name, title);
        }
        return writer;
    }

    /**
     * 标题
     *
     * @param titleList
     * @return
     */
    public static Map<String, String> getTitleCommon(List<BaseField> titleList) {
        Map<String, String> headerAlias = new LinkedHashMap<>();
        headerAlias.put("id", "id");
        for (BaseField baseField : titleList) {
            String title = baseField.getTitle();
            String name = baseField.getName();
            if (name.equals("id")) {
                continue;
            }
            headerAlias.put(name, title);
        }
        return headerAlias;
    }

    public static List<Map<String, Object>> getList(List<BaseItem> baseItemList) {
        List<Map<String, Object>> list = new ArrayList<>();
        if (baseItemList != null) {
            for (BaseItem baseItem : baseItemList) {
                Map<String, Object> map = new LinkedHashMap<>();
                Long id = baseItem.getId();
                map.put("id", id);
                List<BaseField> baseFieldList = baseItem.getBaseFieldList();
                for (BaseField baseField : baseFieldList) {
                    String title = baseField.getTitle();
                    String name = baseField.getName();
                    if (name.equals("id")) {
                        continue;
                    }
                    Object value = baseField.getValue();
                    map.put(name, value);
                }
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 导出Excel数据
     */
    public static void exportExcel(String excelName, Map<String, String> headerAlias, List<?> list, OutputStream outputStream) {
        //通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter();

        if (headerAlias != null) {
            writer.merge(headerAlias.size() - 1, excelName);
            writer.setHeaderAlias(headerAlias);
        }
        if (list == null) {
            list = new ArrayList<>();
        }
        //一次性写出内容，强制输出标题
        writer.write(list, true);
        //out为OutputStream，需要写出到的目标流
        writer.flush(outputStream);
        //关闭writer，释放内存
        writer.close();
    }

    /**
     * 读取Excel文件数据
     *
     * @param entity      实体类
     * @param inputStream Excel文件输入流
     * @return 返回数据集合
     */
    public static <T> List<T> importExcel(Class<T> entity, InputStream inputStream) {
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<T> all = reader.read(1,2,entity);
        return all;
    }
}
