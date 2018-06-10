package com.swsm.system.service;

import com.core.tools.Filter;
import com.swsm.system.model.LoginInfo;

import java.util.List;
import java.util.Map;


/**
 * <p>ClassName: ILoginInfoService</p>
 * <p>Description: 用户登录接口服务</p>
 */
public interface ILoginInfoService {

    /**
     * 
     * <p>Description: 分页查询用户登录信息</p>
     * @param filter 过滤器
     * @return 用户登录信息列表
     */
    public List<LoginInfo> queryLoginInfo(Filter filter);

    /**
     * 
     * <p>Description: 新增一条记录</p>
     * @param userName 登录用户名
     * @param ipAddress 登录ip
     */
    public void saveLoginInfo(String userName, String ipAddress);

    /**
     * 
     * <p>Description: 登录用户注销</p>
     * @param userId 被注销用户id
     * @param logoffTag 注销原因
     */
    public void logOffUser(String userId, String logoffTag);
    
    /**
     * 
     * <p>Description: 登录用户注销,根据用户名注销</p>
     * @param userName 被注销用户
     * @param logoffTag 注销原因
     */
    public void logOffUserByUserName(String userName, String logoffTag);

    /**
     * 
     * <p>Description: 更新登录用户实时访问的模块</p>
     * @param userName 用户名
     * @param modual 访问模块
     */
    public void updateLoginInfo(String userName, String modual);

    /**
     * 
     * <p>Description: 根据用户名以及用户ip判断用户登录是否有效</p>
     * @param userName 用户名
     * @param localIp ip
     * @return 0:ok;(1.正常注销；2.session过期；)3.账号二次登录踢出；4.管理界面踢出；
     */
    public String checkLoginStatus(String userName, String localIp);
    

    /**
     * 
     * <p>Description: 校验该用户是否在其他电脑已经登录</p>
     * @param userName 用户名
     * @param ipAddress ip地址
     * @return 结果，true：标识已经被登录
     */
    public boolean checkUserIsLogin(String userName, String ipAddress);
    
    /**
     * 
     * <p>Description: 通过用户名拿到当前用户的信息</p>
     * @param userName 用户名
     * @return 用户信息
     */
    List<Map<String, Object>> getUserInfoByUserName(String userName);

    /**
     * 
     * <p>Description: 检查所有用户的session是否过期</p>
     */
    public void checkAllSession();

}
