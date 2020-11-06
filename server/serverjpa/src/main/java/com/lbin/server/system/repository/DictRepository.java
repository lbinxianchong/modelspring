package com.lbin.server.system.repository;


import com.lbin.sql.jpa.repository.BaseRepository;
import com.lbin.server.system.domain.Dict;

/**
 * @author
 * @date 2018/8/14
 */
public interface DictRepository extends BaseRepository<Dict, Long> {

    /**
     * 根据标识查询字典数据,且排查指定ID的字典
     * @param name 字典标识
     * @return 字典信息
     */
    Dict findByName(String name);
}
