package com.lbin.sql.jpa.service.impl;


import com.lbin.sql.jpa.annotation.AutowiredBaseModel;
import com.lbin.sql.jpa.data.PageSort;
import com.lbin.sql.jpa.enums.StatusEnum;
import com.lbin.sql.jpa.repository.BaseRepository;
import com.lbin.sql.jpa.service.BaseService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * @author lbin
 * @date 2019/11/05
 */
//@Service
@Transactional
public class BaseServiceImpl<T> implements BaseService<T> {

    //    @Autowired
//    @Qualifier
//    @Resource
    protected BaseRepository<T, Long> baseRepository;

    @PostConstruct
    public void init() {
        if (baseRepository == null) {
            try {
                for (Field declaredField : this.getClass().getDeclaredFields()) {
                    declaredField.setAccessible(true);
                    Object o = declaredField.get(this);
                    if (o instanceof BaseRepository && !declaredField.getName().equals("baseRepository")) {
                        baseRepository = (BaseRepository<T, Long>) o;
                        if (declaredField.isAnnotationPresent(AutowiredBaseModel.class)) {
                            break;
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public BaseServiceImpl() {
    }

    public BaseServiceImpl(BaseRepository<T, Long> baseRepository) {
        this.baseRepository = baseRepository;
    }

    @Override
    public List<T> findAll() {
        return baseRepository.findAll();
    }

    @Override
    public List<T> findAll(T t) {
        Example<T> example = Example.of(t);
        return baseRepository.findAll(example);
    }

    @Override
    public List<T> findAll(Example<T> example) {
        return baseRepository.findAll(example);
    }

    @Override
    public List<T> findAllById(List<Long> longs) {
        return baseRepository.findAllById(longs);
    }

    @Override
    public Page<T> getPageList(Pageable page) {
        return baseRepository.findAll(page);
    }

    @Override
    public Page<T> getPageList(T t) {
        Example<T> example = Example.of(t);
        PageRequest page = PageSort.pageRequest();
        return baseRepository.findAll(example, page);
    }

    @Override
    public Page<T> getPageList(T t, PageRequest page) {
        Example<T> example = Example.of(t);
        return baseRepository.findAll(example, page);
    }

    @Override
    public Page<T> getPageList(Example<T> example) {
        PageRequest page = PageSort.pageRequest();
        return baseRepository.findAll(example, page);
    }

    @Override
    public Page<T> getPageList(Example<T> example, PageRequest page) {
        return baseRepository.findAll(example, page);
    }

    @Override
    public boolean existsById(Long aLong) {
        return baseRepository.existsById(aLong);
    }

    @Override
    public boolean exists(T t) {
        Example<T> example = Example.of(t);
        return baseRepository.exists(example);
    }

    @Override
    public Long count(T t) {
        Example<T> example = Example.of(t);
        return baseRepository.count(example);
    }

    @Override
    public Long count(Example<T> example) {
        return baseRepository.count(example);
    }

    @Override
    @Transactional
    public T getById(Long aLong) {
        return findById(aLong);
    }

    @Override
    @Transactional
    public T findById(Long aLong) {
        return baseRepository.findById(aLong).orElse(null);
    }

    @Override
    @Transactional
    public T getOne(Long aLong) {
        return baseRepository.getOne(aLong);
    }

    @Override
    @Transactional
    public T findOne(T t) {
        Example<T> example = Example.of(t);
        return baseRepository.findOne(example).orElse(null);
    }

    @Override
    @Transactional
    public T save(T entity) {
        return baseRepository.save(entity);
    }

    @Override
    @Transactional
    public List<T> saveAll(List<T> entities) {
        return baseRepository.saveAll(entities);
    }

    @Override
    @Transactional
    public void deleteById(Long aLong) {
        baseRepository.deleteById(aLong);
    }

    @Override
    @Transactional
    public void delete(T entity) {
        baseRepository.delete(entity);
    }

    @Override
    @Transactional
    public void deleteAll() {
        baseRepository.deleteAll();
    }

    @Override
    @Transactional
    public void deleteByIdIn(List<Long> idList) {
        baseRepository.deleteByIdIn(idList);
    }

    @Override
    @Transactional
    public T findByIdAndStatus(Long aLong, Byte status) {
        return baseRepository.findByIdAndStatus(aLong, status);
    }

    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return baseRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }
}