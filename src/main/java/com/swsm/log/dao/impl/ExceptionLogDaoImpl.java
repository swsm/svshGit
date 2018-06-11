package com.swsm.log.dao.impl;

import com.core.dao.impl.BaseDaoImpl;
import com.core.tools.Filter;
import com.swsm.log.model.ExceptionLog;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>ClassName: ExceptionLogDao</p>
 * <p>Description: 异常日志表的数据库操作类</p>
 */
@Repository("exceptionLogDao")
public class ExceptionLogDaoImpl extends BaseDaoImpl<ExceptionLog, String> {

    @SuppressWarnings("unchecked")
    public List<ExceptionLog> getExceptionLog(Filter filter) {
        String countHql;
        countHql = "select count(*)";
        String recordHql;
        recordHql = " from ExceptionLog where delFlag='0'";
        if (checkValid("startOccurDate", filter.getQueryMap())) {
            recordHql += "and createDate >=:startOccurDate ";
        }
        if (checkValid("endOccurDate", filter.getQueryMap())) {
            recordHql += "and createDate <=:endOccurDate ";
        }
        if (checkValid("exceptionType", filter.getQueryMap())) {
            recordHql += "and exceptionType = :exceptionType ";
        }
        if (checkValid("exceptionReason", filter.getQueryMap())) {
            recordHql += "and exceptionReason like :exceptionReason ";
        }
        if (checkValid("remoteIp", filter.getQueryMap())) {
            recordHql += "and remoteIp like :remoteIp ";
        }
        recordHql += "order by createDate desc";
        return findPagesListByHql(countHql + recordHql, recordHql, filter);
    }

    @SuppressWarnings("unchecked")
    public List<ExceptionLog> getExceptionLogConten(String id) {
        String hql = "from ExceptionLog where delFlag='0' and id = '" + id + "'";
        return findByQuery(hql).list();
    }

}
