package com.swsm.system.dao.impl;

import com.core.dao.impl.BaseDaoImpl;
import com.swsm.system.model.DictIdx;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * <p>ClassName: DictIdxDao</p>
 * <p>Description: 字典索引的数据库存取类</p>
 */
@Repository("dictIdxDao")
public class DictIdxDaoImpl extends BaseDaoImpl<DictIdx, String> {

    @SuppressWarnings("unchecked")
    public List<DictIdx> saveOrUpdateDictIndexDemo(String newDictKey) {
        return findByQuery("from DictIdx d where d.delFlag = 0 and d.dictKey ='"
                + newDictKey + "'").list();
    }

    @SuppressWarnings("unchecked")
    public List<DictIdx> saveOrUpdateDictIndexDemo(DictIdx dictIndex, String newDictKey) {
        return findByQuery("from DictIdx d where d.delFlag = 0 and d.id != '" +
                dictIndex.getId() + "' and  d.dictKey ='" + newDictKey + "'").list();
    }


    

}
