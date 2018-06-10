package com.swsm.system.service.impl;

import com.core.dao.impl.BaseDaoImpl;
import com.core.service.impl.EntityServiceImpl;
import com.core.tools.Filter;
import com.swsm.constant.CommonConstants;
import com.swsm.log.dao.impl.AccessLogDaoImpl;
import com.swsm.log.model.AccessLog;
import com.swsm.system.dao.impl.LoginInfoDaoImpl;
import com.swsm.system.dao.impl.UserDaoImpl;
import com.swsm.system.model.LoginInfo;
import com.swsm.system.model.User;
import com.swsm.system.service.ILoginInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>ClassName: LoginInfoServiceImpl</p>
 * <p>Description: 用户登录服务实现类</p>
 */
@Service("loginInfoService")
public class LoginInfoServiceImpl extends EntityServiceImpl<LoginInfo> implements ILoginInfoService {
    
    /**
     * userDao实例化
     */
    @Autowired
    @Qualifier("userDao")
    private UserDaoImpl userDao;
    
    /**
     * loginInfoDao实例化
     */
    @Autowired
    @Qualifier("loginInfoDao")
    private LoginInfoDaoImpl loginInfoDao;

    @Autowired
    @Qualifier("loginInfoDao")
    @Override
    public void setBaseDao(BaseDaoImpl<LoginInfo, String> baseDaoImpl) {
        this.baseDaoImpl = baseDaoImpl;
    }
    @Autowired
    @Qualifier("accessLogDao")
    private AccessLogDaoImpl accessLogDao;
    
    @Override
    public List<LoginInfo> queryLoginInfo(Filter filter) {
        List<String> params;
        params = new ArrayList<>();
        if (checkValid("userName", filter.getQueryMap())) {
            params.add("userName");
        }
        if (checkValid("trueName", filter.getQueryMap())) {
            params.add("trueName");
        }
        if (checkValid("ipAddress", filter.getQueryMap())) {
            params.add("ipAddress");
        }
        List<LoginInfo> list;
        list = this.loginInfoDao.queryLoginInfo(filter, params);
        return list;
    }
    @Override
    public void saveLoginInfo(String userName, String ipAddress) {
        User user = this.userDao.getUserByUserName(userName);
        LoginInfo info = new LoginInfo();
        info.setUserName(userName);
        if (user != null) {
            info.setTrueName(user.getTruename());
        }
        info.setIpAddress(ipAddress);
        info.setLoginTime(new Date());
        info.setLoginStatus(CommonConstants.LOGIN_STATUS_TRUE);
        info.setCreateDate(new Date());
        info.setCreateUser(userName);
        info.setDelFlag(DEL_FALSE);
        this.baseDaoImpl.save(info);
    }

    @Override
    public void logOffUser(String userId, String logoffTag) {
        User user;
        user = this.userDao.getById(User.class, userId);
        this.logOffUserByUserName(user.getUsername(), logoffTag);
    }

    /**
     * 
     * <p>Description: 查找用户登录信息，根据用户名以及状态</p>
     * @param userName 用户名
     * @param loginStatus 登录状态
     * @return 用户登录信息
     */
    private LoginInfo getLoginInfoByUserName(String userName, String loginStatus) {
        List<LoginInfo> list;
        list = this.loginInfoDao.getLoginInfoByUserName(userName, loginStatus);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public void logOffUserByUserName(String userName, String logoffTag) {
        LoginInfo info;
        info = this.getLoginInfoByUserName(userName, CommonConstants.LOGIN_STATUS_TRUE);
        if (info != null) {
            info.setLoginStatus(CommonConstants.LOGIN_STATUS_FALSE);
            info.setLogoffTag(logoffTag);
            info.setUpdateDate(new Date());
            info.setUpdateUser(userName);
            this.baseDaoImpl.update(info);
        }        
    }

    @Override
    public void updateLoginInfo(String userName, String modual) {
        LoginInfo info;
        info = this.getLoginInfoByUserName(userName, CommonConstants.LOGIN_STATUS_TRUE);
        if (info != null) {
            info.setModuleName(modual);
            info.setUpdateDate(new Date());
            info.setUpdateUser(userName);
            this.baseDaoImpl.update(info);
        } 
    }

    @Override
    public String checkLoginStatus(String userName, String localIp) {
        LoginInfo info;
        info = this.checkUserIsLogining(userName, localIp, CommonConstants.LOGIN_STATUS_TRUE);
        if (info != null) {
            return "0";
        }
        LoginInfo loginInfo;
        loginInfo = this.checkUserIsLogining(userName, localIp, CommonConstants.LOGIN_STATUS_FALSE);
        if (loginInfo == null) {
            return null;
        }
        return loginInfo.getLogoffTag();
    }
    
    /**
     * 
     * <p>Description: 根据条件查询用户登录信息</p>
     * @param userName 用户名
     * @param ipAddress ip地址
     * @param status 登录状态
     * @return 结果，true：标识有效
     */
    private LoginInfo checkUserIsLogining(String userName, String ipAddress, String status) {
        List<LoginInfo> list;
        list = this.loginInfoDao.checkUserIsLogining(userName, ipAddress, status);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public boolean checkUserIsLogin(String userName, String ipAddress) {
        List<LoginInfo> list = this.loginInfoDao.checkUserIsLogin(userName, ipAddress);
        return !list.isEmpty();
    }

    @Override
    public List<Map<String, Object>> getUserInfoByUserName(String userName) {
        List<Map<String, Object>> list;
        list = this.loginInfoDao.getUserInfoByUserName(userName);
        return list;
    }

    @Override
    public void checkAllSession() {
        List<AccessLog> list = this.accessLogDao.checkAllSession();
        for(AccessLog a:list){
            if(new Date().getTime() - a.getCreateDate().getTime() > 1800000){
                if(StringUtils.isNotEmpty(a.getUsername())){
                    List<LoginInfo> listr = this.loginInfoDao.getLoginInfoByUserName(a.getUsername(),"1");
                    if(listr.size() != 0){
                        for(LoginInfo l:listr){
                            l.setLoginStatus("0");
                            l.setLogoffTag("2");
                        }
                        this.loginInfoDao.update(listr);
                    }
                }
            }
        }
    }
    
}
