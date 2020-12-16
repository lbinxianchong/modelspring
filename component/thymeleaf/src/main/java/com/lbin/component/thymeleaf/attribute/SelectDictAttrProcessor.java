package com.lbin.component.thymeleaf.attribute;

import cn.hutool.core.bean.BeanUtil;
import com.lbin.common.util.CacheUtil;
import com.lbin.component.thymeleaf.util.DictUtil;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.postprocessor.IPostProcessor;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.expression.*;
import org.thymeleaf.templatemode.TemplateMode;

import java.lang.reflect.Array;
import java.util.*;

/**
 * 根据字典标识生成下拉列表
 * @author
 * @date 2018/8/14
 */
public class SelectDictAttrProcessor extends AbstractAttributeTagProcessor {

    public static final int PRECEDENCE = 1400;
    public static final String ATTR_NAME = "dict";
    public static final String SELECTED_ATTR_NAME= "mo-selected";
    public static final String EMPTY_ATTR_NAME= "mo-empty";

    public SelectDictAttrProcessor(final TemplateMode templateMode, final String dialectPrefix) {
        super(templateMode, dialectPrefix, null, false, ATTR_NAME, true, PRECEDENCE, true);
    }

    public SelectDictAttrProcessor(final TemplateMode templateMode, final String dialectPrefix, String attrName, int precedence) {
        super(templateMode, dialectPrefix, null, false, attrName, true, precedence, true);
    }

    protected Map<String, Object> getValueList(Object expressionResult){
        if (expressionResult==null){
            return null;
        }
        Map<String, Object> valueList = new LinkedHashMap<>();
        if (expressionResult.getClass().isArray()) {
            // 转换数组
            int length = Array.getLength(expressionResult);
            for (int i = 0; i < length; i++) {
                String value = String.valueOf(Array.get(expressionResult, i));
                valueList.put(value, value);
            }
        } else if (expressionResult instanceof Collection) {
            // 装换Collection集合
            Collection list = (Collection) expressionResult;
            if (list instanceof List && list.size() > 0) {
                for (Object item : list) {
                    Object key = BeanUtil.getFieldValue(item, "key");
                    Object value = BeanUtil.getFieldValue(item, "value");
                    if (key == null || value == null) {
                        key = BeanUtil.getFieldValue(item, "id");
                        value = BeanUtil.getFieldValue(item, "name");
                    }
                    valueList.put(String.valueOf(key), String.valueOf(value));
                }
            } else {
                list.forEach(item -> {
                    valueList.put(String.valueOf(item), String.valueOf(item));
                });
            }
        } else if (expressionResult instanceof Map) {
            // 装换Map集合
            Map list = (Map) expressionResult;
            list.forEach((key, item) -> {
                valueList.put(String.valueOf(key), String.valueOf(item));
            });
        }
        return valueList;
    }


    @Override
    protected void doProcess(
            final ITemplateContext context,
            final IProcessableElementTag tag,
            final AttributeName attributeName,
            final String attributeValue,
            final IElementTagStructureHandler structureHandler) {

        // 如果属性值为空或者标签不为select，则不处理
        String elementName = tag.getElementCompleteName();
        if(attributeValue.isEmpty() || !"select".equals(elementName)) {
            return;
        }

        // 获取列表对象，空则不处理
        Map<String, Object> valueList = DictUtil.value(attributeValue);
        if(valueList == null) {
            Object valueData = CacheUtil.valueData(attributeValue);
            valueList = getValueList(valueData);
        }
        if(valueList != null && valueList.size() > 0) {
            doProcess(context, tag, attributeName, attributeValue, structureHandler, valueList);
        };
    }

    @SuppressWarnings("unchecked")
    protected void doProcess(
            final ITemplateContext context,
            final IProcessableElementTag tag,
            final AttributeName attributeName,
            final String attributeValue,
            final IElementTagStructureHandler structureHandler,
            Map<String, Object> valueList) {

        // 获取默认选中值
        String attributeSelectedValue = tag.getAttributeValue(SELECTED_ATTR_NAME);
        Object selectedValue = null;
        final IEngineConfiguration configuration = context.getConfiguration();
        if (attributeSelectedValue != null && !attributeSelectedValue.isEmpty()){
            final IStandardExpressionParser expressionParser = StandardExpressions.getExpressionParser(configuration);
            final IStandardExpression expression = expressionParser.parseExpression(context, attributeSelectedValue);
            final Object expressionResult;
            if (expression != null && expression instanceof FragmentExpression) {
                final FragmentExpression.ExecutedFragmentExpression executedFragmentExpression =
                        FragmentExpression.createExecutedFragmentExpression(context, (FragmentExpression) expression);
                expressionResult =
                        FragmentExpression.resolveExecutedFragmentExpression(context, executedFragmentExpression, true);
            } else {
                expressionResult = expression.execute(context, StandardExpressionExecutionContext.NORMAL);
            }
            if (expressionResult == NoOpToken.VALUE) {
                return;
            }
            selectedValue = (expressionResult == null ? "" : expressionResult);
        }

        // 拼装下拉列表选项
        List<String> keyList = new ArrayList<>();
        if (selectedValue instanceof List){
            ((List) selectedValue).forEach(item -> keyList.add(String.valueOf(item)));
        }else {
            keyList.add(selectedValue == null ? "" : selectedValue.toString());
        }
        StringBuilder optionContent = new StringBuilder();
        valueList.forEach((key, val) -> {
            optionContent.append("<option value = '").append(key);
            if(keyList.contains(String.valueOf(key))){
                optionContent.append("' ").append("selected='selected");
            }
            optionContent.append("'>").append(val).append("</option>");
        });

        // 判断是添加空值选项
        String attributeEmptyValue = tag.getAttributeValue(EMPTY_ATTR_NAME);
        if(attributeEmptyValue != null){
            String emptyStr = "<option value=''>" + attributeEmptyValue + "</option>";
            optionContent.insert(0, emptyStr);
        }

        // 返回新的HTML节点
        final Set<IPostProcessor> postProcessors = configuration.getPostProcessors(getTemplateMode());
        if (postProcessors.isEmpty()) {
            structureHandler.removeAttribute(SELECTED_ATTR_NAME);
            structureHandler.removeAttribute(EMPTY_ATTR_NAME);
            structureHandler.setBody(optionContent, false);
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
