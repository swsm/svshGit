package com.swsm.system.log.service;

import com.core.tools.Filter;
import com.swsm.system.log.model.InterfaceLog;

import java.util.List;


/**
 * <p>ClassName: IInterfaceLogService</p>
 * <p>Description: 接口日志业务处理层接口</p>
 */
public interface IInterfaceLogService {

    /**
     * 
     * <p>Description: 根据条件查询接口日志</p>
     * @param filter 查询条件
     * @return 接口日志
     */
    public List<InterfaceLog> getInterfaceLog(Filter filter);
}
