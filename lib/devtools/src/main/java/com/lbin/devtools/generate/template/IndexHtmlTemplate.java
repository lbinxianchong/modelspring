package com.lbin.devtools.generate.template;

import com.lbin.devtools.generate.domain.Generate;
import com.lbin.devtools.generate.enums.TierType;
import com.lbin.devtools.generate.utils.FileUtil;
import com.lbin.devtools.generate.utils.GenerateUtil;
import com.lbin.devtools.generate.utils.parser.HtmlParseUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

/**
 * @author
 * @date 2018/10/25
 */
public class IndexHtmlTemplate {

    /**
     * 生成页面
     */
    private static String genHtmlBody(Generate generate) throws IOException {
        // 构建数据
        String title = generate.getBasic().getGenTitle();
        String baseUrl = generate.getBasic().getRequestMapping();
        String path = FileUtil.templatePath(IndexHtmlTemplate.class);

        // 获取Jsoup文档对象
        Document document = HtmlParseUtil.document(path);

        // 拼接搜索模块
        Element searchNode = HtmlParseUtil.getJsoup(document, "search");
        StringBuilder searchBuilder = new StringBuilder();
        generate.getFields().forEach(field -> {
            if (field.getQuery() != null && field.getQuery() > 0) {
                String temp = searchNode.toString();
                temp = temp.replace("#{search.title}", field.getTitle());
                temp = temp.replace("#{search.name}", field.getName());
                searchBuilder.append(HtmlParseUtil.tabBreak(searchNode)).append(temp);
            }
        });
        searchNode.after(searchBuilder.toString());
        searchNode.remove();

        StringBuilder tableThBuilder = new StringBuilder();
        StringBuilder tableListBuilder = new StringBuilder();
        generate.getFields().forEach(field -> {
            if (field.isShow()) {

                String thtitle = field.getTitle();
                String thname = field.getName();

                String value = null;
                // 列表信息
                switch (field.getName()) {
                    case "status":
                        value = "${#dicts.dataStatus(item.status)}";
                        break;
                    case "createDate":
                    case "updateDate":
                        value = String.format("${#dates.format(item.%s, 'yyyy-MM-dd HH:mm:ss')}", field.getName());
                        break;
                    default:
                        value = String.format("${item.%s}", field.getName());
                }
                value = "[[" + value + "]]";

                tableThBuilder.append("<th class=\"sortable\" data-field=\"" + thname + "\">" + thtitle + "</th>\n        ");
                tableListBuilder.append("<td>" + value + "</td>\n        ");

            }
        });

        // 替换基本数据
        String html = HtmlParseUtil.html(document);
        html = html.replace("#{title}", title);
        html = html.replace("#{baseUrl}", baseUrl);

        html = html.replace("#{th}", tableThBuilder.toString());
        html = html.replace("#{list}", tableListBuilder.toString());

        return html;
    }

    /**
     * 生成列表页面模板
     */
    public static String generate(Generate generate) {
        String filePath = GenerateUtil.getHtmlFilePath(generate, TierType.INDEX);
        try {
            String content = IndexHtmlTemplate.genHtmlBody(generate);
            GenerateUtil.generateFile(filePath, content);
        } catch (FileAlreadyExistsException e) {
            return GenerateUtil.fileExist(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }
}
