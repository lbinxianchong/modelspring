package com.lbin.server.system.repository;


import com.lbin.sql.jpa.repository.BaseRepository;
import com.lbin.server.system.domain.FileUpload;

/**
 * @author lbin
 * @date 2020/03/02
 */
public interface FileUploadRepository extends BaseRepository<FileUpload, Long> {

    /**
     * 获取文件sha1值的记录
     * @param sha1 文件sha1值
     * @return 文件信息
     */
    FileUpload findBySha1(String sha1);

    /**
     * 获取文件UUID值的记录
     * @param uuid 文件UUID值
     * @return 文件信息
     */
    FileUpload findByUuid(String uuid);
}