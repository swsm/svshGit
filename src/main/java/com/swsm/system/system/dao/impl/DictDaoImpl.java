package com.swsm.system.system.dao.impl;

import com.core.dao.impl.BaseDaoImpl;
import com.core.exception.BaseDaoException;
import com.core.tools.*;
import com.swsm.system.system.model.Dict;
import com.swsm.system.system.model.DictIdx;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>ClassName: DictDao</p>
 * <p>Description: 字典明细的数据库存取类</p>
 */
@Repository("dictDao")
public class DictDaoImpl extends BaseDaoImpl<Dict, String> {
    
    /**
     * <p>Description: 是否使用缓存</p>
     * @return true or false
     */
    public boolean usingCache() {
        return true;
    }

    @SuppressWarnings("unchecked")
    public List<DictIdx> getDictIdxForGrid(Filter filter, List<String> params) {
        String countHql;
        countHql = "select count(*) from DictIdx ";
        String recordHql;
        recordHql = "from DictIdx ";
        String conditionString;
        conditionString = " where delFlag='0' ";
        if (params.contains("dictName")) {
            conditionString += " AND dictName like :dictName";
        }
        return findPagesListByHql(countHql + conditionString, recordHql + conditionString, filter);
    }

    @SuppressWarnings("unchecked")
    public List<Dict> deleteDictIndex(DictIdx dictIdx) {
        return findEntityListByHql("from Dict t where t.parentKey=\'" + dictIdx.getDictKey() + "'" +
                " and t.delFlag = '0'", null);
    }

    @SuppressWarnings("unchecked")
    public List<Dict> getDictForGrid(Filter filter, List<String> params) {
        String countHql;
        countHql = "select count(*) from Dict ";
        String recordHql;
        recordHql = "from Dict ";
        String conditionString;
        conditionString = " where delFlag='0' ";
        conditionString += " AND parentKey = :parentKey";
        if (params.contains("parentKey")) {
            conditionString += " AND parentKey = :parentKey";
        }
        String orderString;
        orderString = " order by dictSort asc";
        return findPagesListByHql(countHql + conditionString, recordHql + conditionString + orderString, filter);
    }

    @SuppressWarnings("unchecked")
    public List<Dict> getDictValueByKey(String parentKey, String dictKey) {
        Map<String, Query> queryMap;
        queryMap = new HashMap<String, Query>();
        queryMap.put("parentKey", new Query(Condition.EQ, parentKey));
        queryMap.put("dictKey", new Query(Condition.EQ, dictKey));
        StringBuilder hql;
        hql = new StringBuilder("from Dict dct");
        hql.append(" where dct.delFlag = '0' and dct.parentKey =:parentKey and dct.dictKey =:dictKey");
        return findByQuery(hql.toString(), queryMap).list();
    }

    @SuppressWarnings("unchecked")
    public List<Dict> getDictKeyByValue(String parentKey, String dictValue) {
        Map<String, Query> queryMap;
        queryMap = new HashMap<String, Query>();
        queryMap.put("parentKey", new Query(Condition.EQ, parentKey));
        queryMap.put("dictValue", new Query(Condition.EQ, dictValue));
        StringBuilder hql;
        hql = new StringBuilder("from Dict dct");
        hql.append(" where dct.delFlag = '0' and dct.parentKey =:parentKey and dct.dictValue =:dictValue");
        return findByQuery(hql.toString(), queryMap).list();
    }

    @SuppressWarnings("unchecked")
    public List<Dict> getDictListyByParentKey(String parentKey) {
        Map<String, Query> queryMap;
        queryMap = new HashMap<String, Query>();
        queryMap.put("parentKey", new Query(Condition.EQ, parentKey));
        StringBuilder hql;
        hql = new StringBuilder("from Dict dct");
        hql.append(" where dct.delFlag = '0' and dct.parentKey =:parentKey");
        return findByQuery(hql.toString(), queryMap).list();
    }
    
    public String getDictValueByParentAndKey(String parentKey, String key) {
        StringBuilder hql;
        hql = new StringBuilder("select dict.dictValue");
        hql.append(" from Dict dict");
        hql.append(" where dict.parentKey = '").append(parentKey).append("'");
        hql.append(" and dict.dictKey = '").append(key).append("'");
        return this.findByQuery(hql.toString()).uniqueResult().toString();
    }

	@SuppressWarnings("unchecked")
	public List<Dict> findDictsByParent(String parentKey, String[] dictKeys)
			throws BaseDaoException {
		StringBuilder builder = new StringBuilder();
		builder.append("from Dict d where d.delFlag = '0' and d.parentKey = '");
		builder.append(parentKey);
		builder.append("'");
		if(dictKeys!=null && dictKeys.length>0){
			String inWhr = MesBaseUtil.createInWhr("d.dictKey", dictKeys);
			builder.append(inWhr);
		}
		List<Dict> dictList=this.findByQuery(builder.toString()).list();
		return dictList;
	}

    @SuppressWarnings({ "unchecked", "deprecation" })
    public List<Dict> getDictByName(Map<String, Query> queryMap) {
        String sql;
        sql = new String();
        sql = "select t.dict_key as dictKey,t.dict_value as dictValue from SYS_DICT t where  t.parent_key=:parentKey";
        Scalar[] scalars;
        scalars = new Scalar[]{
            new Scalar("dictKey" , StringType.INSTANCE),
            new Scalar("dictValue" , StringType.INSTANCE)
        };
        return this.findEntityListBySql(sql, queryMap, scalars, Dict.class);
    }
}
