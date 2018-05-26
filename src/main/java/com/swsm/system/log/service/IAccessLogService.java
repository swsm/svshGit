package com.swsm.system.log.service;

import com.core.tools.Filter;
import com.swsm.system.log.model.AccessLog;

import java.util.List;

/**
 * <p>ClassName: IAccessLogService</p>
 * <p>Description: 访问日志接口</p>
 */
public interface IAccessLogService {

    /**
     * <p>Description: 查询访问日志信息</p>
     * @param filter 过滤器  
     * @return AccessLog集合
     */
    public List<AccessLog> getAccessLog(Filter filter);

    /**
     * <p>Description: 新增访问日志</p>
     * @param localIp 用户IP
     * @param modual 模块名称
     * @param logConten 日志内容
     * @param dispName 用户姓名
     * @param userId 用户ID
     */
    public void insertAccessLog(String localIp, String modual, String logConten, String dispName, String userId);
    
    /**
     * <p>Description: 根据用户名称查询访问日志信息</p>
     * @param userName 用户名
     * @return AccessLog集合
     */
    public List<AccessLog> getAccessLogByUserName(String userName);
    
    /**
     * 
     * <p>Description: 根据用户名称查询最近一条访问日志</p>
     * @param userName 用户名
     * @return 单条记录
     */
    public AccessLog getRecentAccessLog(String userName);
    

}
