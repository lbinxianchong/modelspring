package com.lbin.component.thymeleaf.dialect;

import com.lbin.component.thymeleaf.attribute.SelectDictAttrProcessor;
import com.lbin.component.thymeleaf.attribute.SelectListAttrProcessor;
import com.lbin.component.thymeleaf.factory.ThymeleafExpressionObjectFactory;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author
 * @date 2018/8/14
 */
public class ThymeleafDialect extends AbstractProcessorDialect implements IExpressionObjectDialect {

    private static final String NAME = "ThymeleafDialect";
    private static final String PREFIX = "mo";
    private IExpressionObjectFactory expressionObjectFactory = null;

    public ThymeleafDialect() {
        super(NAME, PREFIX, StandardDialect.PROCESSOR_PRECEDENCE);
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        Set<IProcessor> processors = new LinkedHashSet<IProcessor>();
        processors.add(new SelectDictAttrProcessor(TemplateMode.HTML, dialectPrefix));
        processors.add(new SelectListAttrProcessor(TemplateMode.HTML, dialectPrefix));
        return processors;
    }

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        if (this.expressionObjectFactory == null) {
            this.expressionObjectFactory = new ThymeleafExpressionObjectFactory();
        }
        return this.expressionObjectFactory;
    }
}
