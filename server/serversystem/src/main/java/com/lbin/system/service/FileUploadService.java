package com.lbin.system.service;

import com.lbin.jpa.service.BaseService;
import com.lbin.system.domain.FileUpload;

/**
 * @author lbin
 * @date 2019/10/31
 */
public interface FileUploadService extends BaseService<FileUpload> {

    /**
     * 获取文件sha1值的记录
     * @param sha1 文件sha1值
     * @return 文件信息
     */
    FileUpload getBySha1(String sha1);


    /**
     * 获取文件UUID值的记录
     * @param uuid 文件UUID值
     * @return 文件信息
     */
    FileUpload getByUUID(String uuid);

}