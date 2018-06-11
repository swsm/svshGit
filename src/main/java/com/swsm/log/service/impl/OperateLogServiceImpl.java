package com.swsm.log.service.impl;

import com.core.dao.impl.BaseDaoImpl;
import com.core.service.impl.EntityServiceImpl;
import com.core.tools.Filter;
import com.swsm.log.dao.impl.OperateLogDaoImpl;
import com.swsm.log.model.OperateContent;
import com.swsm.log.service.IOperateLogService;
import com.swsm.log.model.OperateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * <p>
 * ClassName: OperateLogServiceImpl
 * </p>
 * <p>
 * Description: 操作日志表的业务逻辑类
 * </p>
 */
@Service("operateLogService")
public class OperateLogServiceImpl extends EntityServiceImpl<OperateLog> implements IOperateLogService {
    @Autowired
    @Qualifier("operateLogDao")
    @Override
    public void setBaseDao(BaseDaoImpl<OperateLog, String> baseDaoImpl) {
        this.baseDaoImpl = baseDaoImpl;
    }
    /**
     * 
     * 实例化operateLogDao
     */
    @Autowired
    @Qualifier("operateLogDao")
    private OperateLogDaoImpl operateLogDao;
    @Override
    public List<OperateLog> getOperateLog(Filter filter) {
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
        if (checkValid("logContent", filter.getQueryMap())) {
            params.add("logContent");
        }
        if (checkValid("operationUser", filter.getQueryMap())) {
            params.add("operationUser");
        }
        if (checkValid("operateIp", filter.getQueryMap())) {
            params.add("operateIp");
        }
        // 执行查询
        List<OperateLog> list;
        list = this.operateLogDao.getOperateLog(filter, params);
        return list;
    }
    @Override
    public void insertOperateLog(HttpServletRequest request, String dispName, String userId, 
            List<OperateContent> list) throws Exception {
        String localIp;
        localIp = this.getIpAddress(request);
        List<OperateLog> operateLogList;
        operateLogList = new ArrayList<OperateLog>();
        for (OperateContent operateContent : list) {
            OperateLog operateLog;
            operateLog = new OperateLog();
            operateLog.setCreateDate(new Date());
            operateLog.setCreateUser(dispName);
            operateLog.setDelFlag(DEL_FALSE);
            operateLog.setRemoteIp(localIp);
            operateLog.setModual(request.getParameter("modual"));
            operateLog.setLogContent(operateContent.getOperateType() + operateContent.getOperateName());
            operateLog.setUserId(userId);
            operateLog.setUsername(dispName);
            operateLogList.add(operateLog);
        }
        this.baseDaoImpl.save(operateLogList);
    }
    @Override
    public void insertOperateLog(HttpServletRequest request, String dispName, String userId, 
            OperateContent operateContent) throws Exception {
        String localIp;
        localIp = this.getIpAddress(request);
        OperateLog operateLog;
        operateLog = new OperateLog();
        operateLog.setCreateDate(new Date());
        operateLog.setCreateUser(dispName);
        operateLog.setDelFlag(DEL_FALSE);
        operateLog.setRemoteIp(localIp);
        operateLog.setModual(request.getParameter("modual"));
        operateLog.setLogContent(operateContent.getOperateType() + operateContent.getOperateName());
        operateLog.setUserId(userId);
        operateLog.setUsername(dispName);
        this.baseDaoImpl.save(operateLog);
    }
    
    /**
     * <p>Description: 得到客户端IP</p>
     * @param request 请求信息
     * @return ip
     * @throws Exception 异常
     */
    private String getIpAddress(HttpServletRequest request) throws Exception {
        String ipAddress; 
        ipAddress = request.getHeader("X-Forwarded-For");   
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {   
            ipAddress = request.getHeader("Proxy-Client-IP");   
        }   
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {   
            ipAddress = request.getHeader("WL-Proxy-Client-IP");   
        }   
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {   
            ipAddress = request.getRemoteAddr();   
            if ("127.0.0.1".equals(ipAddress)) {
                InetAddress inet;  
                inet = InetAddress.getLocalHost();   
                ipAddress = inet.getHostAddress();   
            }  
        }   
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割   
        if (ipAddress != null && ipAddress.indexOf(",") > 0) {
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));   
        }
        return ipAddress;
    }
}
