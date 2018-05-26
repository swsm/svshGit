package com.swsm.system.log.dao.impl;


import com.core.dao.impl.BaseDaoImpl;
import com.core.tools.Filter;
import com.swsm.system.log.model.AccessLog;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * ClassName: AccessLogDao
 * </p>
 * <p>
 * Description: 访问日志表的数据库操作类
 * </p>
 */
@Repository("accessLogDao")
public class AccessLogDaoImpl extends BaseDaoImpl<AccessLog, String> {

    @SuppressWarnings("unchecked")
    public List<AccessLog> getAccessLog(Filter filter, List<String> params) {
        String countHql;
        countHql = "select count(*) from AccessLog ";
        String recordHql;
        recordHql = "from AccessLog ";
        String conditionString;
        conditionString = "where delFlag='0' ";
        if (params.contains("startDate")) {
            conditionString += "and createDate >=:startDate ";
        }
        if (params.contains("endDate")) {
            conditionString += "and createDate <=:endDate ";
        }
        if (params.contains("modual")) {
            conditionString += "and modual like :modual ";
        }
        if (params.contains("logConten")) {
            conditionString += "and logConten like :logConten ";
        }
        conditionString += "order by createDate desc";
        return findPagesListByHql(countHql + conditionString, recordHql + conditionString, filter);
    }

    @SuppressWarnings("unchecked")
    public List<AccessLog> getAccessLogByUserName(String userName) {
        String countHql;
        countHql = " from AccessLog ";
        String conditionString;
        conditionString = "where delFlag='0'  and username='" + userName + "'";
        conditionString += " order by createDate desc";
        return findByQuery(countHql + conditionString).list();
    }

    @SuppressWarnings("unchecked")
    public List<AccessLog> getAccessLogByUserName(String userName, Filter filter) {
        String countHql;
        countHql = " from AccessLog ";
        String conditionString;
        conditionString = "where delFlag='0'  and username='" + userName + "'";
        conditionString += " order by createDate desc";
        
        
        return findByQuery(countHql + conditionString)
                .setFirstResult(filter.getPageInfo().start)
                .setMaxResults(filter.getPageInfo().limit).list();
    }

    @SuppressWarnings("unchecked")
    public List<AccessLog> checkAllSession() {
        StringBuilder sql = new StringBuilder();
        sql.append("select max(t.create_date) as \"createDate\" ,(select u.username from sys_user u where u.pk_id = t.user_id and rownum = 1) as \"username\" ");
        sql.append("from TT_ACCESS_LOG t group by t.user_id");
        List<AccessLog> list = this.findBySqlQuery(sql.toString()).
                setResultTransformer(Transformers.aliasToBean(AccessLog.class)).list();
        return list;
    }

}

