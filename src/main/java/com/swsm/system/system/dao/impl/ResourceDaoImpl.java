package com.swsm.system.system.dao.impl;

import com.core.dao.impl.BaseDaoImpl;
import com.core.tools.Condition;
import com.core.tools.Query;
import com.core.tools.Scalar;
import com.swsm.system.system.model.Resource;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * <p>ClassName: ResourceDao</p>
 * <p>Description: 用于资源的数据访问层实现</p>
 */
@Repository("resourceDao")
public class ResourceDaoImpl extends BaseDaoImpl<Resource, String> {
    
    public boolean usingCache() {
        return true;
    }

    public Object hasAuthor(String userId, String resCode) {
        Map<String, Query> queryMap;
        queryMap = new HashMap<>();
        queryMap.put("loginName", new Query(Condition.EQ, userId));
        queryMap.put("resCode", new Query(Condition.EQ, resCode));
        String hql;
        hql = " select count(*) from Resource r " 
            + " inner join r.roleList role inner join role.userList user " 
            + " where user.username=:loginName and r.resCode=:resCode ";
        return findByQuery(hql, queryMap).uniqueResult();
    }
    @SuppressWarnings({ "unused", "deprecation" })
    public SQLQuery getResourceTreeByFilter(String parentId, String flag) {
      //执行查询
        StringBuilder sb;
        sb = new StringBuilder();
        sb.append("select t0.PK_ID as id, t0.RES_NAME as resName,")
                .append("(select count(t2.PK_ID) from sys_resource t2 ") 
                .append("where t2.parent_id = t0.PK_ID and t2.enabled = '1' and t2.del_flag = '0') as childCount, ")
                .append("t0.RES_CODE as resCode, t0.RES_TYPE as resType ")
                .append("from SYS_RESOURCE t0 ")
                .append("where t0.del_flag ='0' and t0.enabled ='1' ");
        if (StringUtils.isEmpty(parentId) || "root".equals(parentId)) {
            sb.append("and t0.parent_id is null ");
        } else {
            if (!StringUtils.isEmpty(flag)) {
                sb.append("and (t0.parent_id ='" + parentId + "' or t0.pk_id ='" + parentId + "') ");
            } else {
                sb.append("and t0.parent_id ='" + parentId + "' ");
            }

        }
        sb.append("order by t0.res_order, t0.create_date ");
        SQLQuery query;
        query = this.findBySqlQuery(sb.toString());
        query.addScalar("id", StringType.INSTANCE);
        query.addScalar("childCount", IntegerType.INSTANCE);
        query.addScalar("resName", StringType.INSTANCE);
        query.addScalar("resCode", StringType.INSTANCE);
        query.addScalar("resType", StringType.INSTANCE);
        query.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
        query.setCacheable(false);
        query.setResultTransformer(Transformers.aliasToBean(Resource.class));
        return query;
    }
    @SuppressWarnings("deprecation")
    public SQLQuery getResIdsByRoleId(String roleId) {
        StringBuilder roleBuf;
        roleBuf = new StringBuilder();
        roleBuf.append("select t.res_id as res_id from SYS_ROLE_RESOURCE t where t.role_id='")
            .append(roleId).append("'");
        SQLQuery query;
        query = this.findBySqlQuery(roleBuf.toString());
        query.addScalar("res_id", StringType.INSTANCE);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query;
    }
    
    @SuppressWarnings("unchecked")
    public List<Resource> getResourceTreeForParentByFilter(String resName) {
        Map<String, Query> queryMap;
        queryMap = new HashMap<>();
        String sql;
        sql = "from Resource where delFlag='0' and enabled ='1' and resType in ('1','2') ";
        if (StringUtils.isNotEmpty(resName)) {
            queryMap.put("resName", new Query(Condition.LIKE, resName));
            sql += " and resName like :resName ";
        }
        sql +=" order by resOrder asc";
        return findByQuery(sql, queryMap).list();
    }
    public org.hibernate.Query getResourceTreeForExport(String roleId) {
        StringBuilder sb;
        sb = new StringBuilder("select t.le as \"le\",t.pk_id,t.res_name as \"resName\",t.res_code as \"resCode\",");
        sb.append(" sd.dict_value as \"resType\" from ( select rownum as num,tt.* from ");
        sb.append("(select level as le,CASE connect_by_isleaf");
        sb.append(" WHEN 0 THEN  case when m.parent_id is null then to_char(connect_by_root m.res_order)");
        sb.append(" else to_char(connect_by_root m.res_order || '.' || m.res_order)");
        sb.append(" end else connect_by_root m.res_order");
        sb.append(" || '.' || get_PreOrder(m.parent_id) || '.' || m.res_order END as xh,m.pk_id,");
        sb.append(" m.res_name,m.res_code,m.res_type,m.parent_id,m.res_order,m.enabled,m.belong_system,");
        sb.append(" to_char(m.create_date,'yyyy-mm-dd hh24:mi:ss') as create_date from sys_resource m ");
        sb.append(" start with m.parent_id is null connect by m.parent_id = prior m.pk_id");
        sb.append(" ORDER SIBLINGS BY m.res_order, m.create_date ) tt");
        sb.append(" left join sys_role_resource srr  on srr.res_id = tt.pk_id");
        sb.append(" left join sys_role sr  on sr.pk_id = srr.role_id  ");
        sb.append(" where sr.pk_id ='").append(roleId).append("') t");
        sb.append(" left join sys_dict sd on sd.dict_key = t.res_type ");
        sb.append(" and sd.parent_key = 'SYS_ZYLBDM' and sd.del_flag = '0' order by t.num");
        
        return  getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sb.toString())
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
    }
    public org.hibernate.Query exportResourceList(List<String> params) {
        StringBuilder sb;
        sb = new StringBuilder("select t.pk_id as \"id\",t.parent_id as \"parentId\",t.isleaf as \"isleaf\",");
        sb.append(" t.res_name as \"resName\",t.res_type,t.res_code as \"resCode\",sd.dict_value as \"resType\",");
        sb.append(" tt.res_name as \"preResName\",t.res_order as \"resOrder\",sdd.dict_value as \"belongSystem\",");
        sb.append(" t.create_date as \"createDate\",t.remark as \"remark\"");
        sb.append(" from (select connect_by_isleaf as isleaf,m.pk_id,");
        sb.append(" m.res_name,m.res_code,m.res_type,m.parent_id,m.res_order,m.enabled,m.belong_system,m.remark,");
        sb.append(" to_char(m.create_date,'yyyy-mm-dd hh24:mi:ss') as create_date from sys_resource m");
        sb.append(" where m.belong_system = '1'");
        sb.append(" start with m.parent_id is null connect by m.parent_id = prior m.pk_id");
        sb.append(" ORDER SIBLINGS BY m.res_order, m.create_date) "); 
        sb.append(" t left join sys_resource  tt on t.parent_id = tt.pk_id");
        sb.append(" left join sys_dict sd on sd.dict_key = t.res_type");
        sb.append(" and sd.parent_key = 'SYS_ZYLBDM' and sd.del_flag = '0' ");
        sb.append(" left join sys_dict sdd on sdd.dict_key = t.belong_system ");
        sb.append(" and sdd.parent_key = 'BELONG_SYSTEM' and sdd.del_flag = '0' where 1=1 ");
        if (params.contains("resName")) {
            sb.append(" and t.res_name like :resName");
        }
        if (params.contains("resType")) {
            sb.append(" and t.res_type = :resType");
        }
        String ss;
        ss = "select m.factory_name as id from tm_factory m ";
        return  findBySqlQuery(String.valueOf(ss)).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
    }
    @SuppressWarnings("unchecked")
    public List<Resource> getResourcesByParentId(String loginName, String parentId, List<String> params) {
        Map<String, Query> queryMap;
        queryMap = new HashMap<String, Query>();
        queryMap.put("loginName", new Query(Condition.EQ, loginName));
        String hql;
        hql = " select distinct r from Resource r inner join fetch r.roleList role"
            + " inner join role.userList user  where user.username=:loginName ";
        //2016-10-16：添加用户的删除标识为0
        hql += " and user.delFlag = '0' and r.belongSystem = '1'";
        if (params.contains("parentId")) {
            hql += " and r.parentResource.id is null and r.resType in ('1','2') and r.enabled='1' ";
        } else {
            queryMap.put("parentId", new Query(Condition.EQ, parentId));
            hql += " and r.parentResource.id=:parentId and r.resType in ('1','2') and r.enabled='1' ";
        }
        hql += "order by r.resOrder";
        return findEntityListByHql(hql, queryMap);
    }
    @SuppressWarnings("unchecked")
    public List<Resource> getChildResource(String parentId, List<String> params) {
        Map<String, Query> map;
        map = new HashMap<>();
        String hql;
        if (params.contains("parentId")) {
            hql = "from Resource r where r.parentResource.id is null and r.resType in ('1','2') "
                + "and r.enabled='1' and r.belongSystem = '1' order by r.resOrder asc ";
        } else {
            hql = "from Resource r where r.parentResource.id = :parentId and r.resType in ('1','2')"
                + " and r.enabled='1' and r.belongSystem = '1' order by r.resOrder asc ";
            map.put("parentId", new Query(Condition.EQ, parentId));
        }
        return findEntityListByHql(hql, map);
    }
    public Object getChildCount(String parentId) {
        String hql;
        hql = "select count(*) from Resource where parentResource.id = :parentId and r.resType in ('1','2') ";
        Map<String, Query> map;
        map = new HashMap<String, Query>();
        map.put("parentId", new Query(Condition.EQ, parentId));
        return findByQuery(hql, map).uniqueResult();
    }
    @SuppressWarnings("unchecked")
    public List<Resource> getHavResByLoginName(String loginName, List<String> params) {
        if (params.contains("loginName")) {
            return findEntityListByHql("from Resource order by resOrder", null);
        } else {
            Map<String, Query> map;
            map = new HashMap<>();
            map.put("loginName", new Query(Condition.EQ, loginName));
            return findEntityListByHql("select r from Resource r inner join fetch r.roleList role " +
                    "inner join role.userList user where user.username=:loginName order by r.resOrder", map);
        }
    }
    @SuppressWarnings({ "unchecked", "deprecation" })
    public List<Resource> getResource(String userId) {
        Map<String, Query> queryMap;
        queryMap = new HashMap<>();
        queryMap.put("userId", new Query(Condition.EQ, userId));
        String sql;
        sql = "select C.RES_CODE as resCode from SYS_USER_ROLE t,SYS_ROLE_RESOURCE B,SYS_RESOURCE C "
                + "where t.user_id=:userId and B.Role_Id=t.role_id and B.Res_Id=C.Pk_Id";
        Scalar[] scalars ;
        scalars = new Scalar[]{
            new Scalar("resCode", StringType.INSTANCE)
        };
        return this.findEntityListBySql(sql, queryMap, scalars, Resource.class);
    }
}
