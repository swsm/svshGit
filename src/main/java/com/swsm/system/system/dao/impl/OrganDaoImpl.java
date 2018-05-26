package com.swsm.system.system.dao.impl;

import com.core.dao.impl.BaseDaoImpl;
import com.core.tools.Condition;
import com.core.tools.Query;
import com.swsm.system.system.model.Organ;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>ClassName: OrganDao</p>
 * <p>Description: 用于机构的数据访问层实现</p>
 */
@Repository("organDao")
public class OrganDaoImpl extends BaseDaoImpl<Organ, String> {

    @SuppressWarnings("unchecked")
    public List<Organ> findOrganByPanrentId() {
        Map<String, Query> map;
        map = new HashMap<String, Query>();
        return findEntityListByHql(
                "from Organ o where o.parentOrgan.id is null and o.delFlag=0 order by o.organOrder asc ", map);
    }

    @SuppressWarnings("unchecked")
    public List<Organ> findOrganByPanrentId(String parentId) {
        Map<String, Query> map;
        map = new HashMap<String, Query>();
        map.put("parentId", new Query(Condition.EQ, parentId));
        return findEntityListByHql(
                "from Organ o where o.parentOrgan.id=:parentId and o.delFlag=0 order by o.organOrder asc ", map);
    }

    @SuppressWarnings("unchecked")
    public List<Organ> findOrgan(Organ organ, String userName) {
        return findByQuery("from Organ o where o.delFlag = '0' and o.organCode ='"
                + organ.getOrganCode() + "'").list();
    }

    @SuppressWarnings("unchecked")
    public List<Organ> checkOrgan(Organ organ, String userName) {
        return findByQuery("from Organ o where o.delFlag='0' and o.id != '" 
                + organ.getId() + "' and o.organCode ='" + organ.getOrganCode() + "'").list();
    }

    @SuppressWarnings("unchecked")
    public List<Organ> getLikeOrganByOrganName(List<String> params, String organName) {
        String hql;
        hql = "from Organ  where delFlag = '0' ";
        if (params.contains("organName")) {
            hql += " AND organName like '%" + organName.toLowerCase() + "%'";
        }
        return findByQuery(hql).list();
    }

}
