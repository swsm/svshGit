package com.swsm.log.exception;

import com.alibaba.fastjson.JSON;
import com.core.exception.BaseDaoException;
import com.core.exception.BaseException;
import com.core.exception.BaseServiceException;
import com.swsm.enums.exception.ErrorCode;
import com.swsm.log.model.AccessLog;
import com.swsm.log.service.IAccessLogService;
import com.swsm.log.service.IExceptionLogService;
import com.swsm.login.session.SessionManage;
import com.swsm.login.session.UserSession;
import org.hibernate.JDBCException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * ClassName: ExceptionHandler
 * </p>
 * <p>
 * Description: 异常处理
 * </p>
 */
@Service
public class ExceptionHandler implements HandlerExceptionResolver {
	
	/**
	 * 日志信息
	 */
	private static Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);
    
    /**
     * 异常日志管理Service
     */
    @Autowired
    @Qualifier("exceptionLogService")
    private IExceptionLogService exceptionLogService;

    /**
     * 访问日志管理Service
     */
    @Autowired
    @Qualifier("accessLogService")
    private IAccessLogService accessLogService;
    
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        logger.error(ex.getMessage(),ex);
        
        
        //当前用户
        UserSession userSession=getUserSession(request.getSession());
        
        //对抛出的异常进行包装
        BaseException baseException=setBusinessException(ex);
        
        //保存异常数据至数据库
        saveExceptionLog(userSession,baseException);
        
        //判断是否是ajax请求
        boolean isAjax =isAjax(request);
        
        if(isAjax){
            
            String errorResponse =getAjaxResponse(baseException.getMessage());
            PrintWriter writer = null;
            try {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/html;charset=UTF-8");
                writer = response.getWriter();
                writer.write(errorResponse);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("处理ajax返回异常", e);
            }finally{
                if(null !=writer){
                    writer.close();
                }
            }
            
            return null;
        }else{
            return new ModelAndView("login");
        }
       
    }
    
    /**
     * 
     * <p>Description: 判断是否为ajax请求</p>
     * @param request
     * @return
     */
    private boolean isAjax(HttpServletRequest request){
        
        return  "XMLHttpRequest".equals(request.getHeader("X-Requested-With").toString()) ;
    }
    
    
    /**
     * 
     * <p>Description: 根据捕获的异常转换为业务异常</p>
     * @param ex 捕获异常
     * @return 业务异常
     */
    private BaseException setBusinessException(Exception ex){
        
        //异常转换
        if(ex instanceof JDBCException){//hibernate 异常 统一为数据库运行异常,后期再细化
            return new BaseDaoException(ErrorCode.DB_ERROR,ex);
        }else if (ex instanceof BaseDaoException){//dao层异常
            return (BaseException) ex;
        }else if(ex instanceof BaseServiceException){//service层异常
            return (BaseException) ex;
        }else{//剩余异常统一包装
            return new BaseException(ErrorCode.SYSTEM_ERROR, ex);
        }
        
    }
    
    /**
     * 
     * <p>Description: 从session中获取用户信息</p>
     * @param session
     * @return
     */
    private UserSession getUserSession(HttpSession session){
        SessionManage sessionManage;
        sessionManage = SessionManage.getSessionManage();
        UserSession userSession;
        userSession = sessionManage.getUserSession(session);
        return userSession;
    }
    
    /**
     * 
     * <p>Description: 保存异常至业务库</p>
     * @param ex
     */
    private void saveExceptionLog(UserSession userSession,BaseException ex){
        String userName;
        userName = userSession.getDispName();
        String userId;
        userId = userSession.getUserId();
        
        //前一条访问日志记录
        AccessLog accessLog =
                this.accessLogService.getRecentAccessLog(userName);
        String modual = "登录";
        if (accessLog != null) {
            modual = accessLog.getModual();
        }

        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
             logger.error(e.getMessage(), e);
        }
        
        String localIp;
        localIp = inetAddress.getHostAddress();
        
        String logConten;
        StringWriter strWrit;
        strWrit = new StringWriter();
        PrintWriter printWrit;
        printWrit = new PrintWriter(strWrit);
        ex.printStackTrace(printWrit);
        logConten = strWrit.toString();
        
        
        this.exceptionLogService.insertExceptionLog(localIp, modual, logConten , userName, userId, ex.getMessage());
    }
    
    /**
     * 
     * <p>Description: ajax 默认返回值</p>
     * @return
     */
    private String getAjaxResponse(String message){
        
        Map<String, Object> causeMaps = new HashMap<>();
        causeMaps.put("success", false);
        causeMaps.put("message", message);
        return JSON.toJSONString(causeMaps);
    }
    

}
