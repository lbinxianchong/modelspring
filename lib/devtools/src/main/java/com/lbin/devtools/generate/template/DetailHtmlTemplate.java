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

    private static String[] fixedSort = {"user", "date", "remark"};
    private static String[] ignore = {"status"};

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
        StringBuilder searchBuilder = new StringBuilder();

        List<Field> fields = generate.getFields();
        for (int i = 0; i < fields.size() - 2; i++) {
            Field field1 = fields.get(i);
            Field field2 = fields.get(i);
            String temp = searchNode.toString();

            String trNode1 = field1.getTitle();
            String thText1 = getName(field1.getName());
            String trNode2 = field2.getTitle();
            String thText2 = getName(field2.getName());

            temp = temp.replace("#{text1.title}", trNode1);
            temp = temp.replace("#{text1.name}", thText1);
            temp = temp.replace("#{text2.title}", trNode2);
            temp = temp.replace("#{text2.name}", thText2);

            searchBuilder.append(HtmlParseUtil.tabBreak(searchNode)).append(temp);
            i++;
        }
        searchNode.after(searchBuilder.toString());
        searchNode.remove();


        Element ttextNode = HtmlParseUtil.getJsoup(document, "ttext");
        StringBuilder ttextBuilder = new StringBuilder();
        if (fields.size()%2==1){
            Field field = fields.get(fields.size() - 1);
            String temp = ttextNode.toString();
            temp = temp.replace("#{text1.title}", field.getTitle());
            temp = temp.replace("#{text1.name}", getName(field.getName()));
            ttextBuilder.append(HtmlParseUtil.tabBreak(ttextNode)).append(temp);
        }else {
            Field field1 = fields.get(fields.size() - 2);
            Field field2 = fields.get(fields.size() - 1);
            String temp = ttextNode.toString();

            String trNode1 = field1.getTitle();
            String thText1 = getName(field1.getName());
            String trNode2 = field2.getTitle();
            String thText2 = getName(field2.getName());

            temp = temp.replace("#{text1.title}", trNode1);
            temp = temp.replace("#{text1.name}", thText1);

            ttextBuilder.append(HtmlParseUtil.tabBreak(ttextNode)).append(temp);

            temp = temp.replace("#{text1.title}", trNode2);
            temp = temp.replace("#{text1.name}", thText2);

            ttextBuilder.append(HtmlParseUtil.tabBreak(ttextNode)).append(temp);
        }
        ttextNode.after(ttextBuilder.toString());
        ttextNode.remove();
        String html = HtmlParseUtil.html(document);

        // 替换基本数据
        return html;
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
