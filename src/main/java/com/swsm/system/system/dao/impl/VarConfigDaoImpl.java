package com.swsm.system.system.dao.impl;

import com.core.dao.impl.BaseDaoImpl;
import com.core.tools.Condition;
import com.core.tools.Query;
import com.swsm.system.system.model.VarConfig;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>ClassName: VarConfigDao</p>
 * <p>Description: 配置项管理的数据库存取类</p>
 */
@Repository("varConfigDao")
public class VarConfigDaoImpl extends BaseDaoImpl<VarConfig, String> {

    @SuppressWarnings("unchecked")
    public List<VarConfig> findVarConfigByPanrentId() {
        Map<String, Query> map;
        map = new HashMap<>();
        return findEntityListByHql("from VarConfig var where var.parentVarConfig.id is null and" +
                " var.delFlag=0 order by var.varOrder asc ", map);
    }

    @SuppressWarnings("unchecked")
    public List<VarConfig> findVarConfigByPanrentId(String parentId) {
        Map<String, Query> map;
        map = new HashMap<>();
        map.put("parentId", new Query(Condition.EQ, parentId));
        return findEntityListByHql(
                "from VarConfig var where var.parentVarConfig.id=:parentId " +
                "and var.delFlag=0 order by var.varOrder asc ", map);
    }

    @SuppressWarnings("unchecked")
    public List<VarConfig> checkVarDisplay(VarConfig varConfig) {
        return findByQuery("from VarConfig var where var.delFlag = '0' and var.varDisplay ='"
                + varConfig.getVarDisplay() + "'").list();
    }

    @SuppressWarnings("unchecked")
    public List<VarConfig> checkVarName(VarConfig varConfig) {
        String hql;
        hql = "from VarConfig var where var.delFlag = '0' and var.varName ='"
                + varConfig.getVarName() + "'";
        return findByQuery(hql).list();
    }

}
