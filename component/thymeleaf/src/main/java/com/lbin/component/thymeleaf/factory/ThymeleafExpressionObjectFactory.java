package com.lbin.component.thymeleaf.factory;

import com.lbin.component.thymeleaf.util.CommonUtil;
import com.lbin.component.thymeleaf.util.DictUtil;
import com.lbin.component.thymeleaf.util.PageUtil;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.expression.IExpressionObjectFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author
 * @date 2018/8/14
 */
public class ThymeleafExpressionObjectFactory implements IExpressionObjectFactory {

    public static final String PAGE_UTIL_NAME = "pageUtil";
    public static final PageUtil PAGE_UTIL_OBJECT = new PageUtil();
    public static final String DICT_UTIL_NAME = "dicts";
    public static final DictUtil DICT_UTIL_OBJECT = new DictUtil();
    public static final String COMMON_UTIL_NAME = "commonUtil";
    public static final CommonUtil COMMON_UTIL_OBJECT = new CommonUtil();

    @Override
    public Set<String> getAllExpressionObjectNames() {
        Set<String> names = Collections.unmodifiableSet(
                new LinkedHashSet<String>(
                        Arrays.asList(
                PAGE_UTIL_NAME,
                DICT_UTIL_NAME,
                COMMON_UTIL_NAME
        )));
        return names;
    }

    @Override
    public Object buildObject(IExpressionContext context, String expressionObjectName) {
        if(PAGE_UTIL_NAME.equals(expressionObjectName)){
            return PAGE_UTIL_OBJECT;
        }
        if(DICT_UTIL_NAME.equals(expressionObjectName)){
            return DICT_UTIL_OBJECT;
        }
        if(COMMON_UTIL_NAME.equals(expressionObjectName)){
            return COMMON_UTIL_OBJECT;
        }
        return null;
    }

    @Override
    public boolean isCacheable(String expressionObjectName) {
        return expressionObjectName != null;
    }
}
