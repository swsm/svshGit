package com.swsm.sequence.dao.impl;

import com.core.dao.impl.BaseDaoImpl;
import com.core.tools.Condition;
import com.core.tools.Query;
import com.swsm.sequence.model.SequenceModel;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * ClassName: 序列号Dao
 * </p>
 * <p>
 * Description: 序列号的DAO的实现类
 * </p>
 */
@Repository("sequenceDao")
public class SequenceDaoImpl extends BaseDaoImpl<SequenceModel, String> {

	
    @SuppressWarnings("unchecked")
    public List<SequenceModel> findSequenceModels(String tableName, String loopDateStr, String secondCode) {
        Map<String, Query> queryMap;
        queryMap = new HashMap<String, Query>();
        queryMap.put("tableName", new Query(Condition.EQ, tableName));
        String queryString;
        queryString = "from SequenceModel where tableName =:tableName ";
        /**
         * 存在没有日期的情况 modify by tangsanlin 2017-08-11
         */
        if(loopDateStr != null){
        	 queryMap.put("seqDate", new Query(Condition.EQ, loopDateStr));
        	 queryString += " and seqDate =:seqDate";
        }
        if (secondCode != null) {
            queryMap.put("secondCode", new Query(Condition.EQ, secondCode));
            queryString += " and secondCode =:secondCode";
        }
        return findEntityListByHql(queryString, queryMap);
    }

}
