package com.swsm.system.log.service.impl;

import com.core.dao.impl.BaseDaoImpl;
import com.core.service.impl.EntityServiceImpl;
import com.core.tools.Filter;
import com.swsm.system.log.dao.impl.InterfaceLogDaoImpl;
import com.swsm.system.log.model.InterfaceLog;
import com.swsm.system.log.service.IInterfaceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <p>ClassName: InterfaceLogServiceImpl</p>
 * <p>Description: 接口日志业务处理实现类</p>
 */
@Service("interfaceLogService")
public class InterfaceLogServiceImpl extends EntityServiceImpl<InterfaceLog> implements IInterfaceLogService {

    @Autowired
    @Qualifier("interfaceLogDao")
    @Override
    public void setBaseDao(BaseDaoImpl<InterfaceLog, String> baseDaoImpl) {
        this.baseDaoImpl = baseDaoImpl;
    }

    @Autowired
    @Qualifier("interfaceLogDao")
    private InterfaceLogDaoImpl interfaceLogDao;

    @Override
    public List<InterfaceLog> getInterfaceLog(Filter filter) {
        return this.interfaceLogDao.getInterfaceLog(filter);
    }
}
