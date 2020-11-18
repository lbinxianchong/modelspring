package com.lbin.server.system.controller;

import com.lbin.common.vo.ResultVo;
import com.lbin.server.component.request.ComponentRequest;
import com.lbin.server.system.server.FileUploadServer;
import com.lbin.sql.jpa.validator.BaseValid;
import com.lbin.server.system.domain.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("/system/fileUpload")
public class FileUploadController extends ComponentRequest<FileUpload, BaseValid> {

    @Autowired
    private FileUploadServer fileUploadServer;

    /**
     * 跳转到添加页面
     */
    @GetMapping("/upload")
    public String toUpload() {
        return "/system/fileUpload/upload";
    }

    /**
     * 上传文件
     */
    @PostMapping("/uploadFile")
    @ResponseBody
    public ResultVo uploadFile(
            @RequestParam(value = "prefixPath", defaultValue = "") String prefixPath,
            @RequestParam("file") MultipartFile multipartFile) {
        return fileUploadServer.uploadFile(prefixPath,multipartFile);
    }

    /**
     * 导出模板
     */
    @GetMapping("/download/{id}")
    public void download(@PathVariable("id") FileUpload fileUpload,
                         HttpServletRequest request, HttpServletResponse response) {
        fileUploadServer.download(fileUpload,request,response);
    }

    /**
     * 导出模板
     */
    @GetMapping("/delete/{id}")
    @ResponseBody
    public ResultVo delete(@PathVariable("id") FileUpload fileUpload) {
        return fileUploadServer.delete(fileUpload);
    }


}
