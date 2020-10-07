package com.lbin.system.service.impl;


import com.lbin.jpa.data.PageSort;
import com.lbin.jpa.enums.StatusEnum;
import com.lbin.jpa.repository.BaseRepository;
import com.lbin.jpa.service.BaseService;
import com.lbin.system.domain.FileUpload;
import com.lbin.system.repository.FileUploadRepository;
import com.lbin.system.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author lbin
 * @date 2019/11/05
 */
@Service
@Transactional
public class FileUploadServiceImpl implements FileUploadService {

    @Autowired
    private FileUploadRepository fileUploadRepository;

    @Override
    public List<FileUpload> findAll() {
        return fileUploadRepository.findAll();
    }

    @Override
    public List<FileUpload> findAll(FileUpload t) {
        Example<FileUpload> example = Example.of(t);
        return fileUploadRepository.findAll(example);
    }

    @Override
    public List<FileUpload> findAll(Example<FileUpload> example) {
        return fileUploadRepository.findAll(example);
    }

    @Override
    public List<FileUpload> findAllById(List<Long> longs) {
        return fileUploadRepository.findAllById(longs);
    }

    @Override
    public Page<FileUpload> getPageList(Pageable page) {
        return fileUploadRepository.findAll(page);
    }

    @Override
    public Page<FileUpload> getPageList(FileUpload t) {
        Example<FileUpload> example = Example.of(t);
        PageRequest page = PageSort.pageRequest();
        return fileUploadRepository.findAll(example, page);
    }

    @Override
    public Page<FileUpload> getPageList(FileUpload t, PageRequest page) {
        Example<FileUpload> example = Example.of(t);
        return fileUploadRepository.findAll(example, page);
    }

    @Override
    public Page<FileUpload> getPageList(Example<FileUpload> example) {
        PageRequest page = PageSort.pageRequest();
        return fileUploadRepository.findAll(example, page);
    }

    @Override
    public Page<FileUpload> getPageList(Example<FileUpload> example, PageRequest page) {
        return fileUploadRepository.findAll(example, page);
    }

    @Override
    public boolean existsById(Long aLong) {
        return fileUploadRepository.existsById(aLong);
    }

    @Override
    public boolean exists(FileUpload t) {
        Example<FileUpload> example = Example.of(t);
        return fileUploadRepository.exists(example);
    }

    @Override
    public Long count(FileUpload t) {
        Example<FileUpload> example = Example.of(t);
        return fileUploadRepository.count(example);
    }

    @Override
    public Long count(Example<FileUpload> example) {
        return fileUploadRepository.count(example);
    }

    @Override
    public FileUpload getBySha1(String sha1) {
        return fileUploadRepository.findBySha1(sha1);
    }

    @Override
    public FileUpload getByUUID(String uuid) {
        return fileUploadRepository.findByUuid(uuid);
    }

    @Override
    public FileUpload getById(Long id) {
        return findById(id);
    }

    @Override
    public FileUpload findById(Long id) {
        return fileUploadRepository.findById(id).orElse(null);
    }

    @Override
    public FileUpload getOne(Long aLong) {
        return fileUploadRepository.getOne(aLong);
    }

    @Override
    public FileUpload findOne(FileUpload t) {
        Example<FileUpload> example = Example.of(t);
        return fileUploadRepository.findOne(example).orElse(null);
    }

    @Override
    @Transactional
    public FileUpload save(FileUpload entity) {
        return fileUploadRepository.save(entity);
    }

    @Override
    @Transactional
    public List<FileUpload> saveAll(List<FileUpload> entities) {
        return fileUploadRepository.saveAll(entities);
    }

    @Override
    @Transactional
    public void deleteById(Long aLong) {
        fileUploadRepository.deleteById(aLong);
    }

    @Override
    @Transactional
    public void delete(FileUpload entity) {
        fileUploadRepository.delete(entity);
    }

    @Override
    @Transactional
    public void deleteAll() {
        fileUploadRepository.deleteAll();
    }

    @Override
    @Transactional
    public FileUpload findByIdAndStatus(Long aLong, Byte status) {
        return fileUploadRepository.findByIdAndStatus(aLong, status);
    }

    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return fileUploadRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }
}