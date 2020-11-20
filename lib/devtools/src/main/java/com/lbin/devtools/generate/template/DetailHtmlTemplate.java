package com.lbin.devtools.generate.template;

import cn.hutool.core.util.StrUtil;
import com.lbin.devtools.generate.domain.Field;
import com.lbin.devtools.generate.domain.Generate;
import com.lbin.devtools.generate.enums.TierType;
import com.lbin.devtools.generate.utils.CodeUtil;
import com.lbin.devtools.generate.utils.FileUtil;
import com.lbin.devtools.generate.utils.GenerateUtil;
import com.lbin.devtools.generate.utils.parser.HtmlParseUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @date 2018/10/25
 */
public class DetailHtmlTemplate {

    private static String[] fixedSort = {"remark"};
    private static String[] ignore = {"status"};
    private static String[] fixed = {"createDate", "updateDate", "createBy", "updateBy"};

    /**
     * 创建节点-格式化字表符
     *
     * @param element Jsoup元素对象
     * @param name    节点名称
     * @return 新建的节点对象
     */
    private static Element appendElement(Element element, String name) {
        element.append(HtmlParseUtil.tabBreak(element) + CodeUtil.tabBreak);
        return element.appendElement(name);
    }

    /**
     * 创建固定组合节点-格式化字表符
     *
     * @param fixeds    固定组合节点集合
     * @param groupName 组名称
     * @return 新建的节点对象
     */
    private static Element fixedElement(Element fixeds, String groupName) {
        Element element = fixeds.getElementById(groupName);
        if (element == null) {
            fixeds.append(HtmlParseUtil.tabBreak(fixeds) + CodeUtil.tabBreak);
            element = fixeds.appendElement("tr").attr("id", groupName);
        }
        return element;
    }

    /**
     * 生成页面
     */
    private static String genHtmlBody(Generate generate) throws IOException {
        // 构建数据
        String var = StrUtil.lowerFirst(generate.getBasic().getTableEntity());
        String path = FileUtil.templatePath(DetailHtmlTemplate.class);

        // 获取Jsoup文档对象
        Document document = HtmlParseUtil.document(path);


        // 遍历字段
        Element searchNode = HtmlParseUtil.getJsoup(document, "detail");
        Element ttextNode = HtmlParseUtil.getJsoup(document, "ttext");
        StringBuilder searchBuilder = new StringBuilder();
        StringBuilder ttextBuilder = new StringBuilder();

        List<Field> tempList = new ArrayList<>();
        List<Field> fixedList = new ArrayList<>();
        List<String> fixeds = Arrays.asList(fixed);
        boolean remark = false;

        List<Field> fields = generate.getFields();
        for (Field field : fields) {
            if ("remark".contains(field.getName())) {
                remark = true;
                continue;
            }
            if ("status".contains(field.getName())) {
                continue;
            }
            if (fixeds.contains(field.getName())) {
                fixedList.add(field);
                if (fixedList.size() > 1) {
                    String temp = searchNode.toString();
                    temp = append(temp, fixedList);
                    ttextBuilder.append(HtmlParseUtil.tabBreak(searchNode)).append(temp);
                    fixedList = new ArrayList<>();
                }
                continue;
            }
            tempList.add(field);
            if (tempList.size() > 1) {
                String temp = searchNode.toString();
                temp = append(temp, tempList);
                searchBuilder.append(HtmlParseUtil.tabBreak(searchNode)).append(temp);
                tempList = new ArrayList<>();
            }
        }

        if (tempList.size() == 1) {
            Field field = tempList.get(0);
            String temp = ttextNode.toString();
            temp = append(temp, field);
            searchBuilder.append(HtmlParseUtil.tabBreak(ttextNode)).append(temp);
        }

        if (fixedList.size() == 1) {
            Field field = fixedList.get(0);
            String temp = ttextNode.toString();
            temp = append(temp, field);
            ttextBuilder.append(HtmlParseUtil.tabBreak(ttextNode)).append(temp);
        }

        searchNode.after(searchBuilder.toString());
        ttextNode.after(ttextBuilder.toString());

        searchNode.remove();
        ttextNode.remove();

        // 判断是否需要remark字段
        Element remarkNode = HtmlParseUtil.getJsoup(document, "remark");
        if (!remark) {
            remarkNode.remove();
        }

        String html = HtmlParseUtil.html(document);

        // 替换基本数据
        return html;
    }

    private static String append(String temp, Field field) {
        temp = temp.replace("#{text1.title}", field.getTitle());
        temp = temp.replace("#{text1.name}", getName(field.getName()));
        return temp;
    }

    private static String append(String temp, List<Field> tempList) {
        Field field1 = tempList.get(0);
        Field field2 = tempList.get(1);
        temp = temp.replace("#{text1.title}", field1.getTitle());
        temp = temp.replace("#{text1.name}", getName(field1.getName()));
        temp = temp.replace("#{text2.title}", field2.getTitle());
        temp = temp.replace("#{text2.name}", getName(field2.getName()));
        return temp;
    }

    private static String getName(String name) {
        String thText = "${model.%s}";
        switch (name) {
            case "createBy":
            case "updateBy":
                thText = "${model.%s?.nickname}";
                break;
            case "createDate":
            case "updateDate":
                thText = "${#dates.format(model.%s, 'yyyy-MM-dd HH:mm:ss')}";
                break;
            case "remark":
                break;
            default:
        }
        return String.format(thText, name);
    }

    /**
     * 生成列表页面模板
     */
    public static String generate(Generate generate) {
        String filePath = GenerateUtil.getHtmlFilePath(generate, TierType.DETAIL);
        try {
            String content = DetailHtmlTemplate.genHtmlBody(generate);
            GenerateUtil.generateFile(filePath, content);
        } catch (FileAlreadyExistsException e) {
            return GenerateUtil.fileExist(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }
}
