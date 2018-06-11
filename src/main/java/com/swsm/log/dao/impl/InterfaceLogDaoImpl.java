package com.swsm.log.dao.impl;

import com.core.dao.impl.BaseDaoImpl;
import com.core.tools.Filter;
import com.swsm.log.model.InterfaceLog;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>ClassName: InterfaceLogDaoImpl</p>
 * <p>Description: 接口日志数据库处理操作类</p>
 */
@Repository("interfaceLogDao")
public class InterfaceLogDaoImpl extends BaseDaoImpl<InterfaceLog, String> {

    @SuppressWarnings("unchecked")
    public List<InterfaceLog> getInterfaceLog(Filter filter) {
        StringBuilder hql = new StringBuilder();
        StringBuilder countHql = new StringBuilder();
        countHql.append("select count(*) ");
        hql.append(" from InterfaceLog where delFlag = '0'");
        if (checkValid("interfaceName", filter.getQueryMap())) {
            hql.append(" and interfaceName like :interfaceName");
        }
        if (checkValid("interfaceIp", filter.getQueryMap())) {
            hql.append(" and interfaceIp like :interfaceIp");
        }
        if (checkValid("dataItems", filter.getQueryMap())) {
            hql.append(" and  dataItems like :dataItems");
        }
        if (checkValid("startOccurDate", filter.getQueryMap())) {
            hql.append(" and  createDate >=:startOccurDate");
        }
        if (checkValid("endOccurDate", filter.getQueryMap())) {
            hql.append(" and  createDate <=:endOccurDate");
        }
        hql.append(" order by createDate desc");
        List<InterfaceLog> list;
        list = this.findPagesListByHql(countHql.append(hql).toString(), hql.toString(), filter);
        return list;
    }

}
