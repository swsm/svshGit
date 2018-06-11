package com.swsm.login.service;

import com.core.exception.BaseException;
import com.core.tools.Filter;
import com.swsm.system.model.User;

import java.util.Map;


/**
 * 
 * <p>
 * ClassName: ILoginService
 * </p>
 * <p>
 * Description: 用户登录服务接口
 * </p>
 */
public interface ILoginService {
    /**
     * 
     * <p>
     * Description: 根据登录用户查找该用户信息，如果不存在该用户，返回null
     * </p>
     * 
     * @param loginName 登录名称
     * @return 用户对象
     * @throws BaseException 业务异常 
     */
    public User findUser(String loginName) throws BaseException;

    /**
     * 
     * <p>
     * Description: 根据登录用户工号查找该用户是否可登录
     * </p>
     * 
     * @param workNo 登录人工号
     * @return 用户对象
     * @throws BaseException 业务异常

     */
    public boolean checkLoginFlag(String workNo) throws BaseException;

    /**
     * 
     * <p>
     * Description: 根据登录用户查找该用户全部信息，包括用户基本信息，机构信息，角色信息，如果不存在该用户，返回null
     * </p>
     * 
     * @param loginName 登录名称
     * @return 用户对象
     * @throws BaseException 业务异常
     */
    public Map<String, Object> findUserFullInfo(String loginName) throws BaseException;
    
    
    /**
     * 
     * <p>Description: 根据用户工号查找User</p>
     * @param workNo 用户名
     * @return User,如果不存在则返回null
     */
    public User getUserByWorkNo(String workNo) ;
    
    /**
     * 
     * <p>Description: 根据工号查询用户名</p>
     * @param workNo 工号
     * @param filter 查询条件
     * @return 用户名
     */
    public String getUserNameByWorkNo(String workNo, Filter filter);

    


}
