package com.lbin.system.service.impl;

import com.lbin.jpa.service.impl.BaseServiceImpl;
import com.lbin.system.domain.FileUpload;
import com.lbin.system.repository.FileUploadRepository;
import com.lbin.system.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author lbin
 * @date 2019/11/05
 */
@Service
@Transactional
public class FileUploadServiceImpl extends BaseServiceImpl<FileUpload> implements FileUploadService {

    @Autowired
    private FileUploadRepository fileUploadRepository;

    @PostConstruct
    public void init() {
        baseRepository = fileUploadRepository;
    }

    @Override
    public FileUpload getBySha1(String sha1) {
        return fileUploadRepository.findBySha1(sha1);
    }

    @Override
    public FileUpload getByUUID(String uuid) {
        return fileUploadRepository.findByUuid(uuid);
    }
}