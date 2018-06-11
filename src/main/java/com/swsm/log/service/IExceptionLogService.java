package com.swsm.log.service;

import com.core.tools.Filter;
import com.swsm.log.model.ExceptionLog;
import com.swsm.system.model.User;

import java.util.List;

/**
 * <p>ClassName: IExceptionLogService</p>
 * <p>Description: 异常日志业务接口类</p>
 */
public interface IExceptionLogService {

    /**
     * <p>Description: 查询操作日志信息</p>
     * @param filter 过滤器
     * @return OperateLog集合
     */
    public List<ExceptionLog> getExceptionLog(Filter filter);
    
    /**
     * <p>Description: 根据id查询操作日志内容</p>
     * @param id 查询id
     * @return OperateLog集合
     */
    public List<ExceptionLog> getExceptionLogConten(String id);
    
    /**
     * <p>Description: 新增异常日志</p>
     * @param localIp 用户IP
     * @param modual 模块名称
     * @param logConten 日志内容
     * @param dispName 用户姓名
     * @param userId 用户ID
     */
    public void insertExceptionLog(String localIp, String modual, String logConten, String dispName, String userId);
    
    /**
     * <p>Description: 新增异常日志</p>
     * @param localIp 用户IP
     * @param modual 模块名称
     * @param logConten 日志内容
     * @param dispName 用户姓名
     * @param userId 用户ID
     * @param remark 自定义异常标志
     */
    public void insertExceptionLog(String localIp, String modual, String logConten, String dispName, String userId,String remark);

    /**
     * 
     * <p>Description: 根据用户id查找用户信息</p>
     * @param userId 用户id
     * @return 用户信息
     */
    public User getUserById(String userId);
}
