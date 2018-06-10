package com.swsm.system.dao.impl;

import com.core.dao.impl.BaseDaoImpl;
import com.core.tools.Condition;
import com.core.tools.Filter;
import com.core.tools.Query;
import com.swsm.system.model.LoginInfo;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>ClassName: LoginInfoDao</p>
 * <p>Description: 用户登录信息的数据库存取类</p>
 */
@Repository("loginInfoDao")
public class LoginInfoDaoImpl  extends BaseDaoImpl<LoginInfo, String> {
    
    /**
     * 
     * <p>Description: 查找用户登录信息，根据用户名以及状态</p>
     * @param userName 用户名
     * @param loginStatus 登录状态
     * @return 用户登录信息
     */
    @SuppressWarnings("unchecked")
    public List<LoginInfo> getLoginInfoByUserName(String userName, String loginStatus) {
        Map<String, Query> queryMap;
        queryMap = new HashMap<String, Query>();
        queryMap.put("userName", new Query(Condition.EQ, userName));
        queryMap.put("loginStatus", new Query(Condition.EQ, loginStatus));
        StringBuilder hql;
        hql = new StringBuilder("from LoginInfo l");
        hql.append(" where l.loginStatus = :loginStatus and l.userName = :userName");
        List<LoginInfo> list;
        list = this.findByQuery(hql.toString(), queryMap).list();
       
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<LoginInfo> queryLoginInfo(Filter filter, List<String> params) {
        StringBuilder hql;
        hql = new StringBuilder();
        hql.append(" from LoginInfo li");
        hql.append(" where li.loginStatus = '1'");
        if (params.contains("userName")) {
            hql.append(" and li.userName like :userName ");
        }
        if (params.contains("trueName")) {
            hql.append(" and li.trueName like :trueName ");
        }
        if (params.contains("ipAddress")) {
            hql.append(" and li.ipAddress like :ipAddress ");
        }
        hql.append(" order by li.loginTime desc");
        return findPagesListByHql("select count(*) " + hql.toString(), hql.toString(), filter);
    }

    @SuppressWarnings("unchecked")
    public List<LoginInfo> checkUserIsLogining(String userName, String ipAddress, String status) {
        Map<String, Query> queryMap;
        queryMap = new HashMap<String, Query>();
        queryMap.put("userName", new Query(Condition.EQ, userName));
        queryMap.put("ipAddress", new Query(Condition.EQ, ipAddress));
        queryMap.put("status", new Query(Condition.EQ, status));
        StringBuilder hql;
        hql = new StringBuilder("from LoginInfo li");
        hql.append(" where li.loginStatus = :status");
        hql.append(" and li.userName = :userName and li.ipAddress = :ipAddress");
        hql.append(" order by li.loginTime desc");
        return findByQuery(hql.toString(), queryMap).list();
    }

    @SuppressWarnings("unchecked")
    public List<LoginInfo> checkUserIsLogin(String userName, String ipAddress) {
        Map<String, Query> queryMap;
        queryMap = new HashMap<String, Query>();
        queryMap.put("userName", new Query(Condition.EQ, userName));
        queryMap.put("ipAddress", new Query(Condition.EQ, ipAddress));
        StringBuilder hql;
        hql = new StringBuilder("from LoginInfo li");
        hql.append(" where li.loginStatus = '1'");
        hql.append(" and li.userName = :userName and li.ipAddress != :ipAddress");
        return findByQuery(hql.toString(), queryMap).list();
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getUserInfoByUserName(String userName) {
        String sql;
        sql = " SELECT pk_id id ,true_name trueName FROM sys_login_info" 
            + " WHERE login_status=1 AND user_name='" + userName + "'";
        return findBySqlQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }
}
