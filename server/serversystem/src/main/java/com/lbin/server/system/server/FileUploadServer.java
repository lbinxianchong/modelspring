package com.lbin.server.system.server;

import cn.hutool.core.bean.BeanUtil;
import com.lbin.common.exception.ResultException;
import com.lbin.common.util.EntityBeanUtil;
import com.lbin.common.util.ResultVoUtil;
import com.lbin.common.vo.ResultVo;
import com.lbin.component.fileUpload.data.Upload;
import com.lbin.component.fileUpload.enums.UploadResultEnum;
import com.lbin.component.fileUpload.service.UploadService;
import com.lbin.server.component.server.ComponentServer;
import com.lbin.server.system.domain.FileUpload;
import com.lbin.server.system.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class FileUploadServer extends ComponentServer<FileUpload> {

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private UploadService uploadService;


    /**
     * 上传文件
     */
    public ResultVo uploadFile(String prefixPath, MultipartFile multipartFile) {
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
    public void download(FileUpload fileUpload, HttpServletRequest request, HttpServletResponse response) {
        Upload upload = new Upload();
        BeanUtil.copyProperties(fileUpload,upload);
        upload = uploadService.addPrefix(upload);
        uploadService.downloadFile(upload,response);
    }

    /**
     * 导出模板
     */
    public ResultVo delete(FileUpload fileUpload) {
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
