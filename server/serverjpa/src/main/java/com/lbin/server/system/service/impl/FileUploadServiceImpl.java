package com.lbin.server.system.service.impl;

import com.lbin.sql.jpa.service.impl.BaseServiceImpl;
import com.lbin.server.system.domain.FileUpload;
import com.lbin.server.system.repository.FileUploadRepository;
import com.lbin.server.system.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lbin
 * @date 2019/11/05
 */
@Service
@Transactional
public class FileUploadServiceImpl extends BaseServiceImpl<FileUpload> implements FileUploadService {

    @Autowired
    private FileUploadRepository fileUploadRepository;

    @Override
    public FileUpload getBySha1(String sha1) {
        return fileUploadRepository.findBySha1(sha1);
    }

    @Override
    public FileUpload getByUUID(String uuid) {
        return fileUploadRepository.findByUuid(uuid);
    }
}