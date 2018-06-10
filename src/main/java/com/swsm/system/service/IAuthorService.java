package com.swsm.system.service;


import com.core.exception.BaseException;

/**
 * 
 * <p>
 * ClassName:IAuthorService
 * </p>
 * <p>
 * Description:权限服务接口
 * </p>
 */
public interface IAuthorService {
    /**
     * 
     * <p>
     * Description: 根据登录用户名判断是否存在权限
     * </p>
     * 
     * @param loginName 登录用户名
     * @param resCode 资源RESCODE
     * @return bool
     * @throws BaseException 异常
     */
    public boolean hasAuthor(String loginName, String resCode) throws BaseException;
}
