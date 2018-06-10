package com.swsm.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.core.dao.impl.BaseDaoImpl;
import com.core.service.impl.EntityServiceImpl;
import com.core.tools.Filter;
import com.core.tools.JsonUtil;
import com.core.tools.StringUtil;
import com.swsm.system.dao.impl.OrganDaoImpl;
import com.swsm.system.dao.impl.RoleDaoImpl;
import com.swsm.system.dao.impl.UserDaoImpl;
import com.swsm.system.model.Organ;
import com.swsm.system.model.Role;
import com.swsm.system.model.User;
import com.swsm.system.service.IUserService;
import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * <p>
 * ClassName: UserService
 * </p>
 * <p>
 * Description: 用户管理的业务处理
 * </p>
 */
@Service("userService")
public class UserServiceImpl extends EntityServiceImpl<User> implements IUserService {
    
    /**
     * 角色service
     */
    @Autowired
    @Qualifier("roleService")
    private RoleServiceImpl roleService;
    
    /**
     * 角色dao
     */
    @Autowired
    @Qualifier("roleDao")
    private RoleDaoImpl roleDao;
    
    /**
     * 机构dao
     */
    @Autowired
    @Qualifier("organDao")
    private OrganDaoImpl organDao;
    
    /**
     * userDao
     */
    @Autowired
    @Qualifier("userDao")
    private UserDaoImpl userDao;

    @Autowired
    @Qualifier("userDao")
    @Override
    public void setBaseDao(BaseDaoImpl<User, String> baseDaoImpl) {
        this.baseDaoImpl = baseDaoImpl;
    }

    @Override
    public void updateUserStatus(List<User> users, String status) {
        for (User temp : users) {
            User user;
            user = this.baseDaoImpl.getById(User.class, temp.getId());
            user.setEnabled(status);
            this.baseDaoImpl.update(user);
        }
    }

    @SuppressWarnings({ "unchecked" })
    @Override
    public List<User> queryPagedEntityListForUser(Filter filter, Class<User> clazz) throws Exception {
        List<String> params;
        params = new ArrayList<String>();
        if (checkValid("username", filter.getQueryMap())) {
            params.add("username");
        }
        if (checkValid("truename", filter.getQueryMap())) {
            params.add("truename");
        }
        if (checkValid("workNo", filter.getQueryMap())) {
            params.add("workNo");
        }
        if (checkValid("roleName", filter.getQueryMap())) {
            params.add("roleName");
        }
        String tempStr = "";
        if (checkValid("organIds", filter.getQueryMap())) {
            params.add("organIds");
            
        }
        //查询并返回
        List<Map<String, Object>> list;
        list = this.userDao.queryPagedEntityListForUser(filter, params);
        List<User> userList;
        userList = new LinkedList<User>();
        String jsonStr = "";
        User u;
        u = new User();
        for (Map<String, Object> map : list) {
            jsonStr = JsonUtil.getJsonStringFromMap(map);
            u = JSON.parseObject(jsonStr, User.class);
            userList.add(u);
        }
        //处理用户角色 显示问题  (不因角色的名称或者其它可更改字段的改动而出现显示问题)
        List<User> hqlUser;
        hqlUser = this.baseDaoImpl.findByQuery("from User u where u.delFlag = '0'").list();
        for (int i = 0; i < userList.size(); i++) {
            for (User uRole : hqlUser) {
                if (userList.get(i).getId().equals(uRole.getId())) {
                    String roleNameStr = "";
                    for (Role role : uRole.getRoleList()) {
                        roleNameStr += role.getRoleName() + ", ";
                    }
                    if (roleNameStr.length() > 0) {
                        userList.get(i).setRoleNameStr(roleNameStr.substring(0, roleNameStr.length() - 2));
                    } else {
                        userList.get(i).setRoleNameStr(null);
                    }
                    break;
                }
            }
        }
        return userList;
    }

    /**
     * <p>
     * Description: 根据criteria对象获取实体
     * </p>
     * 
     * @param criteria criteria对象
     * @return list
     */
    public List<User> queryEntityByCriteria(DetachedCriteria criteria) {
        return this.baseDaoImpl.findByCriteria(criteria);
    }

    /**
     * <p>
     * Description: 测试事务
     * </p>
     * 
     * @return 返回值
     */
    public String insertTest() {
        User user;
        user = this.baseDaoImpl.getById(User.class, "12345678123456781234567812345678");
        if (user == null) {
            user = new User();
        }
        user.setTruename("345");

        return user.getTruename();
    }

    @Override
    public List<User> getUserForGrid(Filter filter, Class<User> clazz) throws Exception {
        List<String> params;
        params = new ArrayList<>();
        if (checkValid("userText", filter.getQueryMap())) {
            params.add("userText");
         
        }
        //将查询列表与分页信息转换为json对象送到前台
        List<User> list;
        list = this.userDao.getUserForGrid(filter, params);
        return list;
    }

    @Override
    public String updatePassword(String newPassword, String oldPassword, String username) {
        String flag;
        flag = this.userDao.updatePassword(newPassword, oldPassword, username);
        return flag;
    }

    @Override
    public String passwordRole(String password) {
        boolean isDigit = false;
        boolean isLetter = false;
        String regex = "^[a-z0-9A-Z]+$";
        for (int i = 0; i < password.length(); i++) {
            if (Character.isDigit(password.charAt(i))) { //用char包装类中的方法判断是否包含数字
                isDigit = true;
            }
            if (Character.isLetter(password.charAt(i))) { //用char包装类中的方法判断是否包含字母
                isLetter = true;
            }
        }
        if (password.matches(regex) && isDigit && isLetter) {
            return "true";
        }
        return "false";
    }

    @SuppressWarnings("unchecked")
    @Override
    public User updateUser(Map<String, Object> parameterMap, String[] userInfo) throws IllegalAccessException,
            InvocationTargetException {
        User user ;
        user = this.baseDaoImpl.getById(User.class, parameterMap.get("id") + "");
        user.setRoleList(Collections.EMPTY_SET);
        user.setOrganList(Collections.EMPTY_SET);
        BeanUtils.populate(user, parameterMap);
        this.baseDaoImpl.update(user);
        return user;
    }

    @Override
    public String disableUser(String ids, String username) {
        for (String id : ids.split(",")) {
            User u;
            u = this.findEntity(User.class, id);
            //0 禁用 1 启用
            u.setEnabled("0");
            u.setUpdateDate(new Date());
            u.setUpdateUser(username);
        }
        return "success";
    }
    
    @Override
    public String enableUser(String ids, String username) {
        for (String id : ids.split(",")) {
            User u;
            u = this.findEntity(User.class, id);
            //0 禁用 1 启用
            u.setEnabled("1");
            u.setUpdateDate(new Date());
            u.setUpdateUser(username);
        }
        return "success";
    }
    
    @Override
    public String deleteUser(String ids, String username) {
        for (String id : ids.split(",")) {
            User u;
            u = this.findEntity(User.class, id);
            u.setDelFlag(DEL_TRUE);
            u.setUpdateDate(new Date());
            u.setUpdateUser(username);
        }
        return "success";
    }

    @Override
    public List<Role> getRoleForUserMng(Filter filter, Class<Role> roleClass) {
        // 执行查询
        List<Role> list;
        list = this.roleDao.getRoleForUserMng();
        filter.getPageInfo().count = list.size();
        return list;
    }

    @Override
    public String saveUser(User user, String username) {
        //验证员工工号的唯一性
        List<User> userWorkList;
        userWorkList = this.userDao.checkWorkNo(user,true);
        if (!userWorkList.isEmpty()) {
            return "workNoExist";
        }
        //验证员工用户名的唯一性
        List<User> userNameList;
        userNameList = this.userDao.checkUserName(user,true);
        if (!userNameList.isEmpty()) {
            return "usernameExist";
        }
        
        user.setDelFlag(DEL_FALSE);
        user.setCreateDate(new Date());
        user.setCreateUser(username);
        String roleName = "";
        for (Role r : user.getRoleList()) {
            roleName += this.roleService.findEntity(Role.class, r.getId()).getRoleName();
            roleName += ", ";
        }
        roleName = roleName.substring(0, roleName.length() - 2);
        user.setRoleNameStr(roleName);
        this.baseDaoImpl.save(user);
        return "success";
    }

    @Override
    public String updateUser(User user, String username) {
        //验证员工工号的唯一性
        List<User> validateUserList;
        validateUserList = this.userDao.checkWorkNo(user,false);
//                this.baseDaoImpl.findByQuery(
//                "from User u where u.delFlag='0' and u.id != '" 
//                + user.getId() + "' and u.workNo ='" + user.getWorkNo() + "'").list();
        if (!validateUserList.isEmpty()) {
            return "workNoExist";
        }
        //验证员工用户名的唯一性
        validateUserList = this.userDao.checkUserName(user,false);
//                this.baseDaoImpl.findByQuery(
//                "from User u where u.delFlag='0' and u.id != '" 
//                + user.getId() + "' and u.username ='" + user.getUsername() + "'").list();
        if (!validateUserList.isEmpty()) {
            return "usernameExist";
        }
        
        user.setDelFlag(DEL_FALSE);
        user.setUpdateDate(new Date());
        user.setUpdateUser(username);
        String roleName = "";
        for (Role r : user.getRoleList()) {
            roleName += this.roleService.findEntity(Role.class, r.getId()).getRoleName();
            roleName += ", ";
        }
        roleName = roleName.substring(0, roleName.length() - 2);
        user.setRoleNameStr(roleName);
        this.baseDaoImpl.update(user);
        return "success";
    }

    @Override
    public String getLikeOrganByOrganName(String organName) {
        String organIds = "";
        List<String> params;
        params = new ArrayList<String>();
        if (!StringUtil.isEmpty(organName)) {
            params.add("organName");
        }
        List<Organ> list;
        list = this.organDao.getLikeOrganByOrganName(params, organName);
        for (Organ o : list) {
            organIds += o.getId() + ",";
        }
        if (!StringUtil.isEmpty(organIds)) {
            organIds = organIds.substring(0, organIds.length() - 1);
        }
        return organIds;
    }
}
