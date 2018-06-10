package com.swsm.system.service.impl;

import com.core.dao.impl.BaseDaoImpl;
import com.core.service.impl.EntityServiceImpl;
import com.core.tools.Filter;
import com.swsm.system.dao.impl.ResourceDaoImpl;
import com.swsm.system.dao.impl.RoleDaoImpl;
import com.swsm.system.model.Resource;
import com.swsm.system.model.Role;
import com.swsm.system.service.IRoleService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * <p>
 * ClassName: RoleServiceImpl
 * </p>
 * <p>
 * Description: 角色管理的业务处理
 * </p>
 */
@Service("roleService")
public class RoleServiceImpl extends EntityServiceImpl<Role> implements IRoleService {

    @Autowired
    @Qualifier("roleDao")
    @Override
    public void setBaseDao(BaseDaoImpl<Role, String> baseDaoImpl) {
        this.baseDaoImpl = baseDaoImpl;
    }
    /**
     * 实例化roleDao
     * 
     */
    @Autowired
    @Qualifier("roleDao")
    private RoleDaoImpl roleDao;

    /**
     * 实例化resourceDao
     *
     */
    @Autowired
    @Qualifier("resourceDao")
    private ResourceDaoImpl resourceDao;
    
    @Override
    public List<Role> getRole(Filter filter) {
        List<String> params;
        params = new ArrayList<>();
        if (checkValid("roleName", filter.getQueryMap())) {
            params.add("roleName");
        }
        if (checkValid("roleType", filter.getQueryMap())) {
            params.add("roleType");
        }
        // 执行查询
        List<Role> list;
        list = this.roleDao.getRole(filter, params);
        return list;
    }

    /**
     * 启用或禁用角色
     * 
     * @param roles 角色列表
     * @param status 状态
     */
    public void updateRoleStatus(List<Role> roles, String status) {
        for (Role temp : roles) {
            Role role;
            role = this.findEntity(Role.class, temp.getId());
            role.setEnabled(status);
            this.baseDaoImpl.update(role);
        }
    }

    /**
     * <p>
     * Description: 根据角色Id，查询该角色拥有多少资源，并把资源Id拼凑为@分割的字符串
     * </p>
     * 
     * @param roleId 觉得Id
     * @return 资源id字符串
     */
    public String queryResIdByRole(String roleId) {
        //HQL查询语句
        String hql;
        hql = "select s from Resource s join s.roleList r where r.id='" + roleId + "'";

        StringBuilder resIdStr;
        resIdStr = new StringBuilder();
        List<Resource> resList;
        resList = this.resourceDao.findByQuery(hql).list(); //执行查询
        for (Resource res : resList) {
            resIdStr.append(res.getId()).append("@");
        }
        return resIdStr.toString();
    }

    /**
     * 
     * <p>
     * Description: 更改资源与角色的对应关系
     * </p>
     * 
     * @param roleId 角色id
     * @param resIdStr 资源id字符串
     */
    public void updateRoleResource(String roleId, String resIdStr) {
        Role role;
        role = this.baseDaoImpl.getById(Role.class, roleId);
        String[] resIdArr;
        resIdArr = StringUtils.split(resIdStr, "@");
        Set<Resource> resList;
        resList = new HashSet<>();
        for (String resId : resIdArr) {
            if (StringUtils.isNotEmpty(resId)) {
                resList.add(new Resource(resId, ""));
            }
        }
        role.setResList(resList);
        this.baseDaoImpl.update(role);
    }

    /**
     * <p>Description: 复制角色保存</p>
     * @param resIdArr 资源id数组
     * @param parameterMap 参数表
     * @param userInfo 当前登陆用户信息数组，传入getUserInfo()即可
     * @throws InstantiationException 实例化异常
     * @throws IllegalAccessException 非法读取异常
     * @throws InvocationTargetException 无效反射异常
     */
    public void saveRole(String[] resIdArr, Map<String, Object> parameterMap,
            String[] userInfo) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Role role;
        role = Role.class.newInstance();
        BeanUtils.populate(role, parameterMap);
        Set<Resource> resList;
        resList = new HashSet<>();
        for (String resId : resIdArr) {
            if (StringUtils.isNotEmpty(resId)) {
                resList.add(new Resource(resId, ""));
            }
        }
        role.setResList(resList);
        role.setDelFlag(DEL_FALSE);
        role.setCreateDate(new Date());
        role.setCreateUser(userInfo[1]);
        this.baseDaoImpl.save(role);
    }
    
    /**
     * <p>Description: 新增或修改角色</p>
     * @param objList 需要操作的角色对象列表
     * @param userName 登录人用户名
     */
    public void saveOrUpdateRole(List<Role> objList, String userName) {
        for (Role role : objList) {
            if (StringUtils.isEmpty(role.getId())) {
                //新增
                role.setDelFlag(DEL_FALSE);
                role.setCreateDate(new Date());
                role.setCreateUser(userName);
                this.baseDaoImpl.save(role);
            } else {
                //更新
                role.setDelFlag(DEL_FALSE);
                role.setUpdateDate(new Date());
                role.setUpdateUser(userName);
                Role r;
                r = this.baseDaoImpl.getById(Role.class, role.getId());
                role.setUserList(r.getUserList());
                role.setResList(r.getResList());
                this.baseDaoImpl.getHibernateTemplate().merge(role);
            }
        }
    }

    /**
     * <p>Description: 启用或禁用角色</p>
     * @param enabled 状态
     * @param ids 角色id数组
     */
    public void updateRoleEnabled(String enabled, String[] ids) {
        for (String id : ids) {
            Role role;
            role = this.baseDaoImpl.getById(Role.class, id);
            role.setEnabled(enabled);
            this.baseDaoImpl.update(role);
        }
    }

    /**
     * <p>Description: 删除角色</p>
     * @param ids 角色id数组
     */
    public void deleteRole(String[] ids) {
        for (String id : ids) {
            this.baseDaoImpl.deleteById(Role.class, id);
        }
    }

    @Override
    public void copyRole(String id, String roleName, String roleType, String ignoreDecesion, String remark) {
        Role oldRole;
        oldRole = this.baseDaoImpl.getById(Role.class, id);
        
        Role role;
        role = new Role();
        try {
            BeanUtils.copyProperties(role, oldRole);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        Set<Resource> resList;
        resList = new HashSet<Resource>();
        for (Resource res : oldRole.getResList()) {
            resList.add(new Resource(res.getId(), ""));
        }
        role.setResList(resList);
        role.setCreateDate(new Date());
        role.setUserList(null);
        role.setRoleName(roleName);
        role.setRoleType(roleType);
        role.setIgnoreDecesion(ignoreDecesion);
        role.setRemark(remark);
        this.baseDaoImpl.save(role);
    }

    @Override
    public Role getRoleById(String roleId) {
        return this.baseDaoImpl.getById(Role.class, roleId);
    }
}
