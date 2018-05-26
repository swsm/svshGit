package com.swsm.system.system.dao.impl;

import com.core.dao.impl.BaseDaoImpl;
import com.core.tools.Condition;
import com.core.tools.Filter;
import com.core.tools.Query;
import com.core.tools.Scalar;
import com.swsm.system.system.model.User;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>ClassName: UserDao</p>
 * <p>Description: 用户信息的数据库存取类</p>
 */
@Repository("userDao")
public class UserDaoImpl  extends BaseDaoImpl<User, String> {
    /**
     * 
     * <p>Description: 根据用户名查找User</p>
     * @param userName 用户名
     * @return User,如果不存在则返回null
     */
    @SuppressWarnings("unchecked")
    public User getUserByUserName(String userName) {
        Map<String, Query> queryMap;
        queryMap = new HashMap<String, Query>();
        queryMap.put("userName", new Query(Condition.EQ, userName));
        StringBuilder hql;
        hql = new StringBuilder("from User u");
        hql.append(" where u.username = :userName and u.delFlag = '0'");
        List<User> list;
        list = this.findByQuery(hql.toString(), queryMap).list();
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    
    /**
     * 
     * <p>Description: 根据用户工号查找User</p>
     * @param workNo 用户名
     * @return User,如果不存在则返回null
     */
    @SuppressWarnings("unchecked")
    public User getUserByWorkNo(String workNo) {
        Map<String, Query> queryMap;
        queryMap = new HashMap<String, Query>();
        queryMap.put("workNo", new Query(Condition.EQ, workNo));
        StringBuilder hql;
        hql = new StringBuilder("from User u");
        hql.append(" where u.workNo = :workNo and u.delFlag = '0'");
        List<User> list;
        list = this.findByQuery(hql.toString(), queryMap).list();
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> queryPagedEntityListForUser(Filter filter, List<String> params) {
        StringBuilder sql;
        sql = new StringBuilder("select distinct u.PK_ID as id, u.work_no as workNo, ");
        sql.append(" u.username as username,u.password as password, ");
        sql.append(" u.truename as truename, u.mobile as mobile, ");
        sql.append(" u.telephone as telephone, u.email as email, ");
        sql.append(" u.address as address, u.remark as remark, u.WECHAT_NAME as wechatName,");
        sql.append(" u.DEL_FLAG as delFlag, u.enabled as enabled,organ.pk_id as organId,");
        sql.append(" u.role_name_str as roleNameStr,organ.organ_name as organName,");
        sql.append(" u.CREATE_USER as createUser, u.UPDATE_DATE as updateDate, ");
        sql.append(" u.CREATE_DATE as createDate, u.UPDATE_USER as updateUser ");
        sql.append(" from sys_user u");
        sql.append(" left join sys_user_organ userOrgan on u.pk_id = userOrgan.user_id");
        sql.append(" left join sys_organ organ on userOrgan.organ_id = organ.PK_ID and organ.del_flag = '0' ");
        sql.append(" left join sys_user_role ur on u.pk_id = ur.user_id ");
        sql.append(" left join sys_role r on ur.role_id = r.pk_id ");
        sql.append(" where u.DEL_FLAG = '0'");
        if (params.contains("username")) {
            sql.append(" AND u.username like :username ");
        }
        if (params.contains("truename")) {
            sql.append(" AND u.truename like :truename ");
        }
        if (params.contains("workNo")) {
            sql.append(" AND u.work_no like :workNo ");
        }
        if (params.contains("roleName")) {
            sql.append(" AND r.role_name like :roleName ");
        }
        String tempStr = "";
        if (params.contains("organIds")) {
            tempStr =  " AND (";
            for (String organid : filter.getQueryMap().get("organIds").getValue().toString().split(",")) {
                tempStr += "organ.pk_id = '" + organid + "' or ";
            }
            //去除最后的'or ' 3个字符 
            tempStr = tempStr.substring(0, tempStr.length() - 3);
            tempStr += " ) ";
        }
        sql.append(tempStr);
        String sqlStr;
        sqlStr = sql.toString();
        String obj;
        filter.getQueryMap().remove("organIds");
        obj = this.findBySqlQuery("select count(*) from (" + sqlStr + ") t0", filter.getQueryMap())
                .uniqueResult().toString();
        filter.getPageInfo().count = Long.parseLong(obj);
        return findBySqlQuery(sqlStr, filter.getQueryMap()).setFirstResult(filter.getPageInfo().start)
                .setMaxResults(filter.getPageInfo().limit).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

    @SuppressWarnings("unchecked")
    public List<User> getUserForGrid(Filter filter, List<String> params) {
        String sql;
        sql = "select t.pk_id as id, t.username as username, t.truename as truename "
                + "from sys_user t where t.del_flag = 0";
        if (params.contains("userText")) {
            sql += " and( t.username like :userText or t.truename like :userText)";
        }
        Scalar[] scalars;
        scalars = new Scalar[] {
                new Scalar("id", StringType.INSTANCE),
                new Scalar("username", StringType.INSTANCE),
                new Scalar("truename", StringType.INSTANCE)
        };
        return findPagesListBySql(sql, filter, scalars, User.class);
    }

    @SuppressWarnings("unchecked")
    public List<User> checkWorkNo(User user,boolean isAdd) {
        String hql = "from User u where u.delFlag = '0' and u.workNo = '"+ user.getWorkNo() + "'";
        if(!isAdd){
            hql += " and u.id != '"+ user.getId() + "'";
        }
        return findByQuery(hql).list();
    }

    @SuppressWarnings("unchecked")
    public List<User> checkUserName(User user,boolean isAdd) {
        String hql = "from User u where u.delFlag = '0' and u.username = '"+ user.getUsername() + "'";
        if(!isAdd){
            hql += " and u.id != '"+ user.getId() + "'";
        }
        return findByQuery(hql).list();
    }

    @SuppressWarnings("unchecked")
    public List<User> findUser(String loginName) {
        String hql;
        hql = "from User user where user.username=:username and user.delFlag='0'";
        Map<String, Query> queryMap;
        queryMap = new HashMap<String, Query>();
        Query query;
        query = new Query(Condition.EQ, loginName);
        queryMap.put("username", query);
        return findByQuery(hql, queryMap).list();
    }

    @SuppressWarnings("unchecked")
    public List<User> checkLoginFlag(String workNo) {
        String sql;
        sql = "select pk_id from TM_EMPLOYEE  where HIS_FLAG=0 and del_flag=0 "
                + "and LOGIN_FLAG=0 and EMPLOYEE_NO=:workNo";
        Map<String, Query> queryMap;
        queryMap = new HashMap<String, Query>();
        Query query;
        query = new Query(Condition.EQ, workNo);
        queryMap.put("workNo", query);
        return findBySqlQuery(sql, queryMap).list();
    }

    @SuppressWarnings("unchecked")
    public List<User> getUserNameByWorkNo(String workNo, Filter filter) {
        String hql;
        hql = " select user.username from User user where user.workNo=:workNo";
        return findByQuery(hql, filter.getQueryMap()).list();
    }

    @SuppressWarnings("unchecked")
    public List<User> getUserForMenu(Map<String, Query> queryMap, List<String> params) {
        StringBuilder sql;
        sql = new StringBuilder("select pk_id as id,truename as truename,work_no as workNo ");
        sql.append(" from sys_user where del_Flag=0 ");
        if (params.contains("employeeName")) {
            sql.append(" and (truename like :employeeName or work_No like :employeeName) ");
        }
        sql.append(" and enabled='1'");
        Scalar[] scalars;
        scalars = new Scalar[]{
            new Scalar("id", StringType.INSTANCE),
            new Scalar("truename", StringType.INSTANCE),
            new Scalar("workNo", StringType.INSTANCE)
        };
        return this.findEntityListBySql(sql.toString(), queryMap, scalars, User.class);
    }

    public String updatePassword(String newPassword, String oldPassword, String username) {
        Map<String, Query> queryMap;
        queryMap = new HashMap<String, Query>();
        queryMap.put("username", new Query(Condition.EQ, username));
        String sql;
        sql = "select u.password from sys_user u where u.username =:username and del_flag='0'";
        System.err.println(this.findBySqlQuery(sql, queryMap).list().get(0));
        String opw;
        opw = (String) this.findBySqlQuery(sql, queryMap).list().get(0);
        if (opw.equals(oldPassword)) {
            sql = "update sys_user A set A.password ='" + newPassword + "' " +
                "where A.username =:username and del_flag='0'";
            this.updateBySqlQuery(sql, queryMap);
            return "success";
        } else {
            return "fail";
        }
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, String>> getUserByCode(String value) {
        Map<String, Query> queryMap;
        queryMap = new HashMap<String, Query>();
        StringBuilder sql;
        sql = new StringBuilder("select usr.work_no as \"userCode\", usr.truename as \"userName\"");
        sql.append(" ,usr.pk_id as \"id\", org.pk_id as \"organId\", org.organ_name as \"organName\"");
        sql.append(" from sys_user usr, SYS_USER_ORGAN uo, sys_organ org");
        sql.append(" where usr.pk_id = uo.user_id and uo.organ_id = org.pk_id and usr.del_flag = '0'");
        if (StringUtils.isNotEmpty(value)) {
            queryMap.put("value", new Query(Condition.LIKE, value));
            sql.append(" and (usr.work_no like :value or usr.truename like :value)");
        }
        return this.findBySqlQuery(sql.toString(), queryMap)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getUserInfosByNo(String userNo) {
        StringBuilder sql;
        sql = new StringBuilder("select usr.pk_id as \"id\", usr.work_no as \"workNo\"");
        sql.append(" , usr.truename as \"userName\", usr.username as \"userNo\", org.organ_name as \"organName\"");
        sql.append(" , usr.role_name_str as \"roleNames\", dict.dict_value as \"job\"");
        sql.append(" from sys_user usr left join sys_user_organ uo on usr.pk_id = uo.user_id");
        sql.append(" left join sys_organ org on uo.organ_id = org.pk_id");
        sql.append(" left join tm_employee ee on ee.employee_no = usr.work_no");
        sql.append(" left join sys_dict dict on ee.job_code = dict.dict_key and dict.parent_key = 'BASIC_JOB_CODE'");
        sql.append(" where usr.del_flag = '0'");
        sql.append(" and usr.work_no = '").append(userNo).append("'");
        return this.findBySqlQuery(sql.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

}
