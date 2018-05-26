package com.swsm.system.system.dao.impl;

import com.core.dao.impl.BaseDaoImpl;
import com.core.tools.Filter;
import com.swsm.system.system.model.CodeDefinition;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>ClassName: CodeDefinitionDaoImpl</p>
 * <p>Description: 编码定义表数据访问层接口实现</p>
 */
@Repository("codeDefinitionDao")
public class CodeDefinitionDaoImpl extends BaseDaoImpl<CodeDefinition, String> {

    @SuppressWarnings("unchecked")
    public List<CodeDefinition> queryCodeDefinition(Filter filter) {
        StringBuilder hql;
        hql = new StringBuilder("from CodeDefinition code");
        hql.append(" where code.delFlag = '0'");
        if (checkValid("codeName", filter.getQueryMap())) {
            hql.append(" and code.codeName like :codeName");
        }
        if (checkValid("codeType", filter.getQueryMap())) {
            hql.append(" and code.codeType = :codeType");
        }
        return this.findPagesListByHql("select count(*) " + hql.toString(), hql.toString(), filter);
    }

    public int getSegmentNumById(String id) {
        StringBuilder hql;
        hql = new StringBuilder("select segmentNum");
        hql.append(" from CodeDefinition ");
        hql.append(" where id = '").append(id).append("'");
        return (int) this.findByQuery(hql.toString()).uniqueResult();
    }

}
