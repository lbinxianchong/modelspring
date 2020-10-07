package com.lbin.system.repository;


import com.lbin.jpa.repository.BaseRepository;
import com.lbin.system.domain.FileUpload;

/**
 * @author lbin
 * @date 2020/03/02
 */
public interface FileUploadRepository extends BaseRepository<FileUpload, Long> {
    FileUpload findBySha1(String sha1);

    FileUpload findByUuid(String uuid);
}