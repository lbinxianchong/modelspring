package com.lbin.system.controller;


import cn.hutool.core.bean.BeanUtil;
import com.lbin.common.exception.ResultException;
import com.lbin.common.util.ResultVoUtil;
import com.lbin.common.vo.ResultVo;
import com.lbin.component.fileUpload.data.Upload;
import com.lbin.component.fileUpload.enums.UploadResultEnum;
import com.lbin.jpa.controller.BaseController;
import com.lbin.common.util.EntityBeanUtil;
import com.lbin.system.domain.FileUpload;
import com.lbin.component.fileUpload.service.UploadService;
import com.lbin.system.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/system/fileUpload")
public class FileUploadController extends BaseController<FileUpload> {

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private UploadService uploadService;

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
        try {
            String Sha1 = uploadService.getFileSha1(multipartFile);
            FileUpload fileUpload = fileUploadService.getBySha1(Sha1);
            if (fileUpload==null){
                Upload upload = uploadService.uploadFile(multipartFile, prefixPath);
                upload = uploadService.removePrefix(upload);
                EntityBeanUtil.copyProperties(upload,fileUpload);
                save(fileUpload);
            }
            return ResultVoUtil.success("上传成功",fileUpload);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVoUtil.error("上传失败");
        }
    }

    /**
     * 导出模板
     */
    @GetMapping("/download/{id}")
    public void download(@PathVariable("id") FileUpload fileUpload,
                                 HttpServletRequest request, HttpServletResponse response) {
        Upload upload = new Upload();
        BeanUtil.copyProperties(fileUpload,upload);
        upload = uploadService.addPrefix(upload);
        uploadService.downloadFile(upload,response);
    }

    /**
     * 导出模板
     */
    @GetMapping("/delete/{id}")
    @ResponseBody
    public ResultVo delete(@PathVariable("id") FileUpload fileUpload) {
        try {
            Upload upload = new Upload();
            EntityBeanUtil.copyProperties(fileUpload,upload);
            upload = uploadService.addPrefix(upload);
            boolean delete = uploadService.delete(upload);
            if (!delete){
                throw new ResultException(UploadResultEnum.NO_FILE_Delete);
            }
            fileUploadService.deleteById(fileUpload.getId());
            return ResultVoUtil.success("成功");
        }catch (Exception e){
            e.printStackTrace();
            return ResultVoUtil.error("失败，请重新操作");
        }
    }


}
