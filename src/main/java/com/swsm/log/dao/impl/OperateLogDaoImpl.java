package com.swsm.log.dao.impl;


import com.core.dao.impl.BaseDaoImpl;
import com.core.tools.Filter;
import com.swsm.log.model.OperateLog;
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
@Repository("operateLogDao")
public class OperateLogDaoImpl extends BaseDaoImpl<OperateLog, String> {
    @SuppressWarnings("unchecked")
    public List<OperateLog> getOperateLog(Filter filter, List<String> params) {
        String countHql;
        countHql = "select count(*) from OperateLog ";
        String recordHql;
        recordHql = "from OperateLog ";
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
        if (params.contains("logContent")) {
            conditionString += "and logContent like :logContent ";
        }
        if (params.contains("operationUser")) {
            conditionString += "and username like :operationUser ";
        }
        if (params.contains("operateIp")) {
            conditionString += "and remoteIp like :operateIp ";
        }
        conditionString += "order by createDate desc";
        return findPagesListByHql(countHql + conditionString, recordHql + conditionString, filter);
    }


}

