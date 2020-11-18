package com.lbin.server.model.annotation;

import cn.hutool.core.util.StrUtil;
import com.lbin.common.enums.ResultEnum;
import com.lbin.common.exception.ResultException;
import com.lbin.common.util.CacheUtil;
import com.lbin.common.util.HttpServletUtil;
import com.lbin.common.util.SpringContextUtil;
import com.lbin.server.component.request.ComponentRequest;
import com.lbin.server.component.server.ComponentServer;
import com.lbin.server.component.util.InitUtil;
import com.lbin.sql.jpa.repository.BaseRepository;
import com.lbin.sql.jpa.service.BaseService;
import com.lbin.sql.jpa.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

//@Controller
@RequestMapping({"/model/*/", "/api/model/*/"})
public class ModelController<T, S> extends ComponentRequest<T, S> {

    @Override
    public void init() {
        thymeleafPath = "/system/model";
        super.init();
    }

}
