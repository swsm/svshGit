package com.swsm.system.log.service.impl;

import com.core.dao.impl.BaseDaoImpl;
import com.core.service.impl.EntityServiceImpl;
import com.core.tools.Filter;
import com.swsm.system.log.dao.impl.ExceptionLogDaoImpl;
import com.swsm.system.log.model.ExceptionLog;
import com.swsm.system.log.service.IExceptionLogService;
import com.swsm.system.system.dao.impl.UserDaoImpl;
import com.swsm.system.system.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>ClassName: ExceptionLogServiceImpl</p>
 * <p>Description: 异常操作日志业务实现类</p>
 */
@Service("exceptionLogService")
public class ExceptionLogServiceImpl extends EntityServiceImpl<ExceptionLog> implements IExceptionLogService {

    @Autowired
    @Qualifier("exceptionLogDao")
    @Override
    public void setBaseDao(BaseDaoImpl<ExceptionLog, String> baseDaoImpl) {
        this.baseDaoImpl = baseDaoImpl;
    }
    /**
     * 实例化exceptionLogDao
     */
    @Autowired
    @Qualifier("exceptionLogDao")
    private ExceptionLogDaoImpl exceptionLogDao;
    
    @Autowired
    @Qualifier("userDao")
    private UserDaoImpl userDao;
    
    @Override
    public List<ExceptionLog> getExceptionLog(Filter filter) {
        List<ExceptionLog> list;
        list = this.exceptionLogDao.getExceptionLog(filter);
        return list;
    }

    @Override
    public void insertExceptionLog(String localIp, String modual, String logConten, String dispName, String userId) {
        insertExceptionLog(localIp, modual, logConten, dispName, userId, "");
    }

    
    @Override
    public void insertExceptionLog(String localIp, String modual, String logConten, 
            String dispName, String userId, String remark) {
        ExceptionLog exceptionLog;
        exceptionLog = new ExceptionLog();
        exceptionLog.setCreateDate(new Date());
        exceptionLog.setCreateUser(dispName);
        exceptionLog.setDelFlag(DEL_FALSE);
        exceptionLog.setLogContent(logConten);
        exceptionLog.setModual(modual);
        exceptionLog.setRemoteIp(localIp);
        exceptionLog.setUserId(userId);
        exceptionLog.setUsername(dispName);
        exceptionLog.setRemark(remark);
        this.baseDaoImpl.save(exceptionLog);
    }
    
    
    @Override
    public List<ExceptionLog> getExceptionLogConten(String id) {
        List<ExceptionLog> list;
        list = this.exceptionLogDao.getExceptionLogConten(id);
        return list;
    }

    @Override
    public User getUserById(String userId) {
        return this.userDao.getById(User.class, userId);
    }

}
