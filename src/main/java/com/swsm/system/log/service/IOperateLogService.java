package com.swsm.system.log.service;

import com.core.tools.Filter;
import com.swsm.system.log.model.OperateContent;
import com.swsm.system.log.model.OperateLog;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * <p>ClassName: IOperateLogService</p>
 * <p>Description: 操作日志接口</p>
 */
public interface IOperateLogService {

    /**
     * <p>Description: 查询操作日志信息</p>
     * @param filter 过滤器
     * @return OperateLog集合
     */
    public List<OperateLog> getOperateLog(Filter filter);

    /**
     * <p>Description: 新增多条操作日志信息，用于增删改等操作的日志记录，
     * list是OperateContent操作日志内容集合，OperateContent由operateType（表示增加、修改、删除等）
     * 和operateName（表示xx名称，如：角色名称）组成
     * </p>
     * @param request 请求,获取客户端IP、操作模块名称
     * @param dispName 操作用户姓名
     * @param userId 操作用户ID
     * @param list OperateContent集合
     * @throws Exception 异常
     */
    public void insertOperateLog(HttpServletRequest request, String dispName, String userId, 
            List<OperateContent> list) throws Exception;
    
    /**
     * <p>Description: 新增单条操作日志信息，用于增删改等操作的日志记录，
     * OperateContent操作日志内容，OperateContent由operateType（表示增加、修改、删除等）
     * 和operateName（表示xx名称，如：角色名称）组成
     * </p>
     * @param request 请求,获取客户端IP、操作模块名称
     * @param dispName 操作用户姓名
     * @param userId 操作用户ID
     * @param operateContent 操作日志内容
     * @throws Exception 异常
     */
    public void insertOperateLog(HttpServletRequest request, String dispName, String userId, 
            OperateContent operateContent) throws Exception;

}
