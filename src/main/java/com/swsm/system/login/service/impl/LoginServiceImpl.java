package com.swsm.system.login.service.impl;

import com.core.exception.BaseException;
import com.core.tools.Filter;
import com.swsm.system.login.service.ILoginService;
import com.swsm.system.system.dao.impl.UserDaoImpl;
import com.swsm.system.system.model.Organ;
import com.swsm.system.system.model.Role;
import com.swsm.system.system.model.User;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * 
 * <p>
 * ClassName: LoginServiceImpl
 * </p>
 * <p>
 * Description: 用户登录服务实现类
 * </p>
 */
@Service("loginService")
public class LoginServiceImpl implements ILoginService {

    /**
     * 日志信息
     */
    private static Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    /**
     * 用户表DAO
     */
    @Autowired
    @Qualifier("userDao")
    private UserDaoImpl userDao;

    @Override
    public User findUser(String loginName) throws BaseException {
        try {
            List<User> list;
            list = this.userDao.findUser(loginName);
            if (list.isEmpty()) {
                return null;
            } else {
                for(User u:list){
                    if("1".equals(u.getEnabled())){
                        return u;
                    }
                }
                return list.get(0);
            }
        } catch (HibernateException e) {
            logger.error(e.getMessage(), e);
            throw new BaseException(e.getMessage());
        }
    }

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
    public boolean checkLoginFlag(String workNo) throws BaseException {
        try {
            List<User> list;
            list = this.userDao.checkLoginFlag(workNo);
            return list.isEmpty();
        } catch (HibernateException e) {
            logger.error(e.getMessage(), e);
            throw new BaseException(e.getMessage());
        }
    }

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
    @SuppressWarnings("unchecked")
    public Map<String, Object> findUserFullInfo(String loginName) throws BaseException {
        User user;
        user = this.findUser(loginName);
        Map<String, Object> map;
        map = new HashMap<String, Object>();
        if (user == null ||"0".equals(user.getEnabled())) {
            return Collections.EMPTY_MAP;
        }
        map.put("user", user);
        Set<Organ> organSet;
        organSet = user.getOrganList();
        Set<Role> roleSet;
        roleSet = user.getRoleList();
        logger.debug(organSet.size() + "" + roleSet.size());
        map.put("organs", organSet);
        map.put("roles", roleSet);
        return map;
    }
    
    public String getUserNameByWorkNo(String workNo, Filter filter) {
        List<User> list;
        list = this.userDao.getUserNameByWorkNo(workNo, filter);
        return list.get(0).getUsername();
    }
    
    
    public User getUserByWorkNo(String workNo) {
        return this.userDao.getUserByWorkNo(workNo);
    }
    
}
