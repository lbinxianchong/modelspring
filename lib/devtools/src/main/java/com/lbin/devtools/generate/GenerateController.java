package com.lbin.devtools.generate;


import com.lbin.common.util.EnumUtil;
import com.lbin.common.util.ResultVoUtil;

import com.lbin.common.vo.ResultVo;

import com.lbin.devtools.generate.domain.Generate;
import com.lbin.devtools.generate.enums.*;
import com.lbin.devtools.generate.template.*;
import com.lbin.devtools.generate.utils.GenerateUtil;

import com.lbin.devtools.generate.utils.TemplateUtil;
import com.lbin.devtools.generate.utils.jAngel.nodes.Document;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author
 * @date 2018/8/14
 */
@Controller
@RequestMapping("/dev/code")
public class GenerateController {

    @GetMapping
    public String index(Model model) {
        model.addAttribute("basic", DefaultValue.getBasic());
        model.addAttribute("fieldList", DefaultValue.fieldList());
        model.addAttribute("fieldType", EnumUtil.enumToMap(FieldType.class));
        model.addAttribute("fieldQuery", EnumUtil.enumToMap(FieldQuery.class));
        model.addAttribute("fieldVerify", EnumUtil.enumToMap(FieldVerify.class));
        return "/devtools/generate/index";
    }

    @PostMapping("/save")
    @ResponseBody
    public ResultVo save(@RequestBody Generate generate) {
        Map<String, String> fieldMap = new LinkedHashMap<>();
        if (generate.getTemplate().isEntity()) {
            Document document = EntityTemplate.genClazzBody(generate);
            String filePath = TemplateUtil.generate(generate, document, TierType.DOMAIN);
            fieldMap.put("实体类", filePath);
        }
        if (generate.getTemplate().isValidator()) {
            Document document = ValidatorTemplate.genClazzBody(generate);
            String filePath = TemplateUtil.generate(generate, document, TierType.VALID);
            fieldMap.put("验证类", filePath);
        }
        if (generate.getTemplate().isRepository()) {
            String filePath = TemplateUtil.generate(generate, TierType.DAO, RepositoryTemplate.genImports(generate),RepositoryTemplate.class);
            fieldMap.put("数据访问层", filePath);
        }
        if (generate.getTemplate().isService()) {
            String filePath = TemplateUtil.generate(generate, TierType.SERVICE, ServiceTemplate.genImports(generate),ServiceTemplate.class);
            fieldMap.put("服务层", filePath);
            String filePathimpl = TemplateUtil.generate(generate, TierType.SERVICE_IMPL, ServiceImplTemplate.genImports(generate),ServiceImplTemplate.class);
            fieldMap.put("服务实现层", filePathimpl);
        }
        if (generate.getTemplate().isServer()) {
            String filePath = TemplateUtil.generate(generate, TierType.SERVER, ServerTemplate.genImports(generate),ServerTemplate.class);
            fieldMap.put("服务",filePath);
        }
        if (generate.getTemplate().isController()) {
            String filePath = TemplateUtil.generate(generate, TierType.CONTROLLER, ControllerTemplate.genImports(generate),ControllerTemplate.class);
            fieldMap.put("控制器", filePath);
        }
        if (generate.getTemplate().isModel()) {
            String filePath = TemplateUtil.generate(generate, TierType.MODEL_CONTROLLER, ModelControllerTemplate.genImports(generate),ModelControllerTemplate.class);
            fieldMap.put("模版控制器", filePath);
        }
        if (generate.getTemplate().isIndex()) {
            fieldMap.put("列表页面", IndexHtmlTemplate.generate(generate));
        }
        if (generate.getTemplate().isAdd()) {
            fieldMap.put("添加页面", AddHtmlTemplate.generate(generate));
        }
        if (generate.getTemplate().isDetail()) {
            fieldMap.put("详细页面", DetailHtmlTemplate.generate(generate));
        }


        return ResultVoUtil.success(fieldMap);
    }

}
