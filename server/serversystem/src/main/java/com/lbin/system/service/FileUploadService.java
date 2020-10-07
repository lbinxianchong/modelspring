package com.lbin.system.service;

import com.lbin.jpa.enums.StatusEnum;
import com.lbin.system.domain.FileUpload;
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
public interface FileUploadService {


    /**
     * 获取列表数据
     *
     * @return
     */
    List<FileUpload> findAll();

    /**
     * 获取列表数据
     *
     * @param t 查询实体对象
     * @return
     */
    List<FileUpload> findAll(FileUpload t);

    /**
     * 获取列表数据
     *
     * @param example 查询实例
     * @return
     */
    List<FileUpload> findAll(Example<FileUpload> example);

    /**
     * 获取列表数据
     *
     * @param longs 查询ID列表
     * @return
     */
    List<FileUpload> findAllById(List<Long> longs);

    /**
     * 获取分页列表数据
     *
     * @param pageable 查询分页实例
     * @return 返回分页数据
     */
    Page<FileUpload> getPageList(Pageable pageable) ;

    /**
     * 获取分页列表数据
     *
     * @param t 查询实例
     * @return 返回分页数据
     */
    Page<FileUpload> getPageList(FileUpload t) ;

    /**
     * 获取分页列表数据
     *
     * @param t 查询实例
     * @param pageable 查询分页实例
     * @return 返回分页数据
     */
    Page<FileUpload> getPageList(FileUpload t, PageRequest pageable) ;

    /**
     * 获取分页列表数据
     *
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<FileUpload> getPageList(Example<FileUpload> example) ;

    /**
     * 获取分页列表数据
     *
     * @param example 查询实例
     * @param pageable 查询分页实例
     * @return 返回分页数据
     */
    Page<FileUpload> getPageList(Example<FileUpload> example, PageRequest pageable) ;

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
    boolean exists(FileUpload t);

    /**
     * 获取数据数量
     *
     * @param t 查询实体对象
     * @return
     */
    Long count(FileUpload t) ;

    /**
     * 获取数据数量
     *
     * @param example 查询实例
     * @return
     */
    Long count(Example<FileUpload> example);

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

    /**
     * 根据ID查询数据
     *
     * @param id 主键ID
     */
    FileUpload getById(Long id);

    /**
     * 根据ID查询数据
     *
     * @param id 主键ID
     */
    FileUpload findById(Long id) ;

    /**
     * 根据ID查询数据
     *
     * @param id 主键ID
     */
    FileUpload getOne(Long id) ;

    /**
     * 根据实体对象查询数据
     *
     * @param entity 查询实体对象
     */
    FileUpload findOne(FileUpload entity) ;

    /**
     * 保存数据
     *
     * @param entity 实体对象
     */
    @Transactional
    FileUpload save(FileUpload entity) ;

    /**
     * 保存数据
     *
     * @param entities 实体对象列表
     */
    @Transactional
    List<FileUpload> saveAll(List<FileUpload> entities) ;

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
    void delete(FileUpload entity) ;

    /**
     * 删除所有数据
     *
     */
    @Transactional
    void deleteAll() ;

    /**
     * 根据id与状态查询对象
     *
     * @param id
     * @param status
     * @return
     */
    @Transactional
    FileUpload findByIdAndStatus(Long id, Byte status);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     *
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) ;


}