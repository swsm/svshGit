package com.swsm.system.dao.impl;

import com.core.dao.impl.BaseDaoImpl;
import com.core.tools.Filter;
import com.swsm.system.model.Role;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * <p>ClassName: RoleDao</p>
 * <p>Description: 用于角色的数据访问层实现</p>
 */
@Repository("roleDao")
public class RoleDaoImpl extends BaseDaoImpl<Role, String> {

    @SuppressWarnings("unchecked")
    public List<Role> getRole(Filter filter, List<String> params) {
        String countHql;
        countHql = "select count(*) from Role ";
        String recordHql;
        recordHql = "from Role ";
        String conditionString;
        conditionString = " where delFlag='0' ";
        if (params.contains("roleName")) {
            conditionString += " and roleName like :roleName";
        }
        if (params.contains("roleType")) {
            conditionString += " and roleType = :roleType";
        }
        return findPagesListByHql(countHql + conditionString, recordHql + conditionString, filter);
    }

    @SuppressWarnings("unchecked")
    public List<Role> getRoleForUserMng() {
        String hql;
        hql = "select id as id, roleName as roleName from Role where delFlag='0' and enabled = '1'";
        return findByQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

}
