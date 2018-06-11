package com.swsm.log.service.impl;

import com.core.dao.impl.BaseDaoImpl;
import com.core.service.impl.EntityServiceImpl;
import com.core.tools.Filter;
import com.swsm.log.model.AccessLog;
import com.swsm.log.service.IAccessLogService;
import com.swsm.log.dao.impl.AccessLogDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * <p>
 * ClassName: AccessLogServiceImpl
 * </p>
 * <p>
 * Description: 访问日志表的业务逻辑类
 * </p>
 */
@Service("accessLogService")
public class AccessLogServiceImpl extends EntityServiceImpl<AccessLog> implements IAccessLogService {

    @Autowired
    @Qualifier("accessLogDao")
    @Override
    public void setBaseDao(BaseDaoImpl<AccessLog, String> baseDaoImpl) {
        this.baseDaoImpl = baseDaoImpl;
    }

    /**
     * 实例化accessLogDao
     */
    @Autowired
    @Qualifier("accessLogDao")
    private AccessLogDaoImpl accessLogDao;

    /**
     * <p>Description: 查询访问日志信息</p>
     *
     * @param filter 过滤器  accessLo
     * @return AccessLog集合
     */
    public List<AccessLog> getAccessLog(Filter filter) {
        List<String> params;
        params = new ArrayList<String>();
        if (checkValid("startDate", filter.getQueryMap())) {
            params.add("startDate");
        }
        if (checkValid("endDate", filter.getQueryMap())) {
            params.add("endDate");
        }
        if (checkValid("modual", filter.getQueryMap())) {
            params.add("modual");
        }
        if (checkValid("logConten", filter.getQueryMap())) {
            params.add("logConten");
        }
        // 执行查询
        List<AccessLog> list;
        list = this.accessLogDao.getAccessLog(filter, params);
        return list;
    }

    @Override
    public void insertAccessLog(String localIp, String modual, String logConten, String dispName, String userId) {
        AccessLog accessLog;
        accessLog = new AccessLog();
        accessLog.setCreateDate(new Date());
        accessLog.setCreateUser(dispName);
        accessLog.setDelFlag(DEL_FALSE);
        accessLog.setRemoteIp(localIp);
        accessLog.setModual(modual);
        accessLog.setLogConten(logConten);
        accessLog.setUserId(userId);
        accessLog.setUsername(dispName);
        this.accessLogDao.save(accessLog);
    }

    @Override
    public List<AccessLog> getAccessLogByUserName(String userName) {
        // 执行查询
        List<AccessLog> list;
        list = this.accessLogDao.getAccessLogByUserName(userName);
        return list;
    }

    @Override
    public AccessLog getRecentAccessLog(String userName) {
        Filter filter = new Filter();
        //查询单条记录
        filter.getPageInfo().limit = 1;
        List<AccessLog> list;
        list = accessLogDao.getAccessLogByUserName(userName, filter);
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0);
        }

        return null;
    }

    @Override
    public List<AccessLog> queryPagedEntityList(Filter filter, Class<AccessLog> clazz) throws Exception {
        return null;
    }

}
