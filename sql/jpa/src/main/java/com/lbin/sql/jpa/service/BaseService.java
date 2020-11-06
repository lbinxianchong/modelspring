package com.lbin.sql.jpa.service;

import com.lbin.sql.jpa.enums.StatusEnum;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author lbin
 * @date 2019/10/31
 */
public interface BaseService<T> {


    /**
     * 获取列表数据
     *
     * @return
     */
    List<T> findAll();

    /**
     * 获取列表数据
     *
     * @param t 查询实体对象
     * @return
     */
    List<T> findAll(T t);

    /**
     * 获取列表数据
     *
     * @param example 查询实例
     * @return
     */
    List<T> findAll(Example<T> example);

    /**
     * 获取列表数据
     *
     * @param longs 查询ID列表
     * @return
     */
    List<T> findAllById(List<Long> longs);

    /**
     * 获取分页列表数据
     *
     * @param pageable 查询分页实例
     * @return 返回分页数据
     */
    Page<T> getPageList(Pageable pageable) ;

    /**
     * 获取分页列表数据
     *
     * @param t 查询实例
     * @return 返回分页数据
     */
    Page<T> getPageList(T t) ;

    /**
     * 获取分页列表数据
     *
     * @param t 查询实例
     * @param pageable 查询分页实例
     * @return 返回分页数据
     */
    Page<T> getPageList(T t, PageRequest pageable) ;

    /**
     * 获取分页列表数据
     *
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<T> getPageList(Example<T> example) ;

    /**
     * 获取分页列表数据
     *
     * @param example 查询实例
     * @param pageable 查询分页实例
     * @return 返回分页数据
     */
    Page<T> getPageList(Example<T> example, PageRequest pageable) ;

    /**
     * 判断实例存在
     *
     * @param id 主键ID
     * @return
     */
    boolean existsById(Long id);

    /**
     * 判断实例存在
     *
     * @param t 查询实体对象
     * @return
     */
    boolean exists(T t);

    /**
     * 获取数据数量
     *
     * @param t 查询实体对象
     * @return
     */
    Long count(T t) ;

    /**
     * 获取数据数量
     *
     * @param example 查询实例
     * @return
     */
    Long count(Example<T> example);

    /**
     * 根据ID查询数据
     *
     * @param id 主键ID
     */
    T getById(Long id);

    /**
     * 根据ID查询数据
     *
     * @param id 主键ID
     */
    T findById(Long id) ;

    /**
     * 根据ID查询数据
     *
     * @param id 主键ID
     */
    T getOne(Long id) ;

    /**
     * 根据实体对象查询数据
     *
     * @param entity 查询实体对象
     */
    T findOne(T entity) ;

    /**
     * 保存数据
     *
     * @param entity 实体对象
     */
    @Transactional
    T save(T entity) ;

    /**
     * 保存数据
     *
     * @param entities 实体对象列表
     */
    @Transactional
    List<T> saveAll(List<T> entities) ;

    /**
     * 根据ID删除数据
     *
     * @param id
     */
    @Transactional
    void deleteById(Long id) ;

    /**
     * 根据实体对象删除数据
     *
     * @param entity 实体对象
     */
    @Transactional
    void delete(T entity) ;

    /**
     * 删除所有数据
     *
     */
    @Transactional
    void deleteAll() ;

    /**
     * 删除多条数据
     *
     */
    @Transactional
    void deleteByIdIn(List<Long> idList);

    /**
     * 根据id与状态查询对象
     *
     * @param id
     * @param status
     * @return
     */
    @Transactional
    T findByIdAndStatus(Long id, Byte status);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     *
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) ;
}