package com.lbin.server.system.service.impl;

import com.lbin.common.util.CacheUtil;
import com.lbin.sql.jpa.service.impl.BaseServiceImpl;
import com.lbin.server.system.domain.Dict;
import com.lbin.server.system.repository.DictRepository;
import com.lbin.server.system.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lbin
 * @date 2019/11/05
 */
@Service
@Transactional
public class DictServiceImpl extends BaseServiceImpl<Dict> implements DictService {

    @Autowired
    private DictRepository dictRepository;

    @Override
    public Dict save(Dict entity) {
        Dict dict = super.save(entity);
        saveCache(dict);
        return dict;
    }

    @Override
    public List<Dict> saveAll(List<Dict> entities) {
        List<Dict> list = super.saveAll(entities);
        for (Dict dict : list) {
            saveCache(dict);
        }
        return list;
    }

    @Override
    public void reset(){
        CacheUtil.clearCache(CacheUtil.dictCache);
        List<Dict> list = dictRepository.findAll();
        for (Dict dict : list) {
            saveCache(dict);
        }
    }

    private void saveCache(Dict dict){
        String name = dict.getName();
        CacheUtil.clearCache(CacheUtil.dictCache,name);
        String value = dict.getValue();
        String[] outerSplit = value.split(",");
        Map<String, String> map = new LinkedHashMap<>();
        for (String osp : outerSplit) {
            String[] split = osp.split(":");
            if(split.length > 1){
                map.put(split[0], split[1]);
            }
        }
        CacheUtil.saveCache(CacheUtil.dictCache,name,map);
    }

}