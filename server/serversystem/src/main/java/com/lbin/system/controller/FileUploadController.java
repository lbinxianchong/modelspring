package com.lbin.system.controller;


import com.lbin.common.exception.ResultException;
import com.lbin.common.util.ResultVoUtil;
import com.lbin.common.vo.ResultVo;
import com.lbin.component.fileUpload.data.Upload;
import com.lbin.component.fileUpload.enums.UploadResultEnum;
import com.lbin.jpa.enums.StatusEnum;
import com.lbin.jpa.utils.EntityBeanUtil;
import com.lbin.jpa.utils.StatusUtil;
import com.lbin.system.domain.FileUpload;
import com.lbin.system.server.UploadService;
import com.lbin.system.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/system/fileUpload")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private UploadService uploadService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
//    @RequiresPermissions("system:fileUpload:index")
    public String index(Model model, FileUpload fileUpload) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.contains());

        // 获取数据列表
        Example<FileUpload> example = Example.of(fileUpload, matcher);
        Page<FileUpload> list = fileUploadService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/system/fileUpload/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
//    @RequiresPermissions("system:fileUpload:add")
    public String toAdd() {
        return "/system/fileUpload/add";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/upload")
//    @RequiresPermissions("system:fileUpload:add")
    public String toUpload() {
        return "/system/fileUpload/upload";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
//    @RequiresPermissions("system:fileUpload:edit")
    public String toEdit(@PathVariable("id") FileUpload fileUpload, Model model) {
        model.addAttribute("fileUpload", fileUpload);
        return "/system/fileUpload/add";
    }

    /**
     * 保存添加/修改的数据
     */
    @PostMapping("/save")
//    @RequiresPermissions({"system:fileUpload:add", "system:fileUpload:edit"})
    @ResponseBody
    public ResultVo save(FileUpload fileUpload) {
        // 复制保留无需修改的数据
        if (fileUpload.getId() != null) {
            FileUpload beFileUpload = fileUploadService.getById(fileUpload.getId());
            EntityBeanUtil.copyProperties(beFileUpload, fileUpload);
        }

        // 保存数据
        fileUploadService.save(fileUpload);
        return ResultVoUtil.SAVE_SUCCESS;
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
                fileUpload = new FileUpload(upload);
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
        Upload upload = fileUpload.getUpload();
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
            Upload upload = fileUpload.getUpload();
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

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
//    @RequiresPermissions("system:fileUpload:detail")
    public String toDetail(@PathVariable("id") FileUpload fileUpload, Model model) {
        model.addAttribute("fileUpload", fileUpload);
        return "/system/fileUpload/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
//    @RequiresPermissions("system:fileUpload:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (fileUploadService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}
