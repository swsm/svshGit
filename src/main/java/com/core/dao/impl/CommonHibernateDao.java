/**
 * CommonHibernateDao.java
 * Created at 2017-3-7
 * Created by Administrator
 * Copyright (C) 2017 BROADTEXT SOFTWARE, All rights reserved.
 */
package com.core.dao.impl;

import com.core.dto.ParamterDto;
import com.core.entity.PageModel;
import com.core.exception.BaseDaoException;
import com.core.tools.Condition;
import com.core.tools.Scalar;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.transform.Transformers;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * ClassName: CommonHibernateDao
 * </p>
 * <p>
 * Description: 数据访问的简单实现，这里封装了关于数据库的一些常用查询或更新操作，主要是基于SQL与HQL的
 * </p>
 */
public class CommonHibernateDao extends BaseHibernateDao {

    /**
     * <p>
     * Description: 是否使用缓存
     * </p>
     * 
     * @return true or false
     */
    protected boolean usingCache() {
        return false;
    }

    /**
     * 
     * <p>
     * Description: 通过SQL执行更新/插入/删除操作，参数需要用:变量名传递进来，如:employeeName
     * </p>
     * 
     * @param queryString SQL字符串
     * @param pmSet 参数集合
     * @throws BaseDaoException 数据访问异常
     */
    public void updateBySqlQuery(String queryString, Set<ParamterDto> pmSet) throws BaseDaoException {
        Query query;
        query = this.getSessionFactory().getCurrentSession().createSQLQuery(queryString);
        this.setExecuteQueryParams(query, pmSet);
        query.executeUpdate();
    }

    /**
     * 
     * <p>
     * Description: 通过SQL执行更新/插入/删除操作，参数需要用:变量名传递进来，如:employeeName
     * </p>
     * 
     * @param queryString SQL字符串
     * @param pmSet 参数列表
     * @throws BaseDaoException 数据访问异常
     */
    public void executeSql(String queryString, Set<ParamterDto> pmSet) throws BaseDaoException {
        Query query;
        query = this.getSessionFactory().getCurrentSession().createSQLQuery(queryString);
        this.setExecuteQueryParams(query, pmSet);
        query.executeUpdate();
    }

    /**
     * 
     * <p>
     * Description: 通过HQL执行更新/插入/删除操作，参数需要用:变量名传递进来，如:employeeName
     * </p>
     * 
     * @param queryString HQL字符串
     * @param pmSet 参数列表
     * @throws BaseDaoException 数据访问异常
     */
    public void updateByQuery(String queryString, Set<ParamterDto> pmSet) throws BaseDaoException {
        Query query;
        query = this.getSessionFactory().getCurrentSession().createQuery(queryString);
        this.setExecuteQueryParams(query, pmSet);
        query.executeUpdate();
    }

    /**
     * 
     * <p>
     * Description: 通过HQL执行更新/插入/删除操作，参数需要用:变量名传递进来，如:employeeName
     * </p>
     * 
     * @param queryString HQL字符串
     * @param pmSet 参数列表
     * @throws BaseDaoException 数据访问异常
     */
    public void executeHql(String queryString, Set<ParamterDto> pmSet) throws BaseDaoException {
        Query query;
        query = this.getSessionFactory().getCurrentSession().createQuery(queryString);
        this.setExecuteQueryParams(query, pmSet);
        query.executeUpdate();
    }

    /**
     * 
     * <p>
     * Description: 构造一个不带查询条件的HQL查询，防止SQL注入问题
     * 对于要first,max,fetchsize,cache,cacheRegion等诸多设置的函数,可以在返回Query后自行设
     * 
     * <pre>
     * dao.getQuery(hql).setMaxResult(100).setCacheable(true).list();
     * </pre>
     * 
     * </p>
     * 
     * @param queryString HQL字符串
     * @return model对象列表
     * @throws BaseDaoException 数据访问异常
     */
    public Query findByQuery(String queryString) throws BaseDaoException {
        Query query;
        query = this.getSessionFactory().getCurrentSession().createQuery(queryString);
        query.setCacheable(usingCache());
        return query;
    }

    /**
     * 
     * <p>
     * Description: 构造一个带查询条件的HQL查询，防止SQL注入问题
     * 对于要first,max,fetchsize,cache,cacheRegion等诸多设置的函数,可以在返回Query后自行设
     * 
     * <pre>
     * dao.getQuery(hql).setMaxResult(100).setCacheable(true).list();
     * </pre>
     * 
     * </p>
     * 
     * @param queryString HQL字符串
     * @param pmSet 参数列表
     * @return model对象列表
     * @throws BaseDaoException 数据访问异常
     */
    public Query findByQuery(String queryString, Set<ParamterDto> pmSet) throws BaseDaoException {
        Query query;
        query = this.getSessionFactory().getCurrentSession().createQuery(queryString);
        query.setCacheable(usingCache());
        this.setFindQueryParams(query, pmSet);
        return query;
    }

    /**
     * 
     * <p>
     * Description: 构造基于SQL的查询，不带查询条件，防止SQL注入问题
     * 对于要first,max,fetchsize,cache,cacheRegion等诸多设置的函数,可以在返回Query后自行设
     * 
     * <pre>
     * dao.findBySqlQuery(sql).setMaxResult(100).setCacheable(true).list();
     * </pre>
     * 
     * </p>
     * 
     * @param queryString SQL字符串
     * @return model对象列表
     * @throws BaseDaoException 数据访问异常
     */
    public SQLQuery findBySqlQuery(String queryString) throws BaseDaoException {
        SQLQuery query;
        query = this.getSessionFactory().getCurrentSession().createSQLQuery(queryString);
        query.setCacheable(usingCache());
        return query;
    }

    /**
     * 
     * <p>
     * Description: 狗仔一个基于SQL的查询，带查询条件，防止SQL注入问题
     * 对于要first,max,fetchsize,cache,cacheRegion等诸多设置的函数,可以在返回Query后自行设
     * 
     * <pre>
     * dao.findBySqlQuery(sql).setMaxResult(100).setCacheable(true).list();
     * </pre>
     * 
     * </p>
     * 
     * @param queryString SQL字符串
     * @param pmSet 参数列表
     * @return model对象列表
     * @throws BaseDaoException 数据访问异常
     */
    public SQLQuery findBySqlQuery(String queryString, Set<ParamterDto> pmSet) throws BaseDaoException {
        SQLQuery query;
        query = this.getSessionFactory().getCurrentSession().createSQLQuery(queryString);
        query.setCacheable(usingCache());
        this.setFindQueryParams(query, pmSet);
        return query;
    }

    /**
     * <p>
     * Description: 根据构造基于SQL的查询条件进行不翻页的查询，返回满足条件的全部记录
     * 
     * <pre>
     * Scalar[] scalars = new Scalar[] { new Scalar(&quot;id&quot;, Hibernate.STRING), new Scalar(&quot;employeeName&quot;, Hibernate.STRING) };
     * </pre>
     * 
     * </p>
     * 
     * @param sql 查询字符串
     * @param pmSet 查询条件
     * @param scalars 结果集映射数组
     * @param clazz 结果集往哪个类上进行映射
     * @return 查询结果
     * @throws BaseDaoException Dao异常
     */
    @SuppressWarnings("rawtypes")
    public List findEntityListBySql(String sql, Set<ParamterDto> pmSet, Scalar[] scalars, Class clazz)
            throws BaseDaoException {
        SQLQuery queryRecords;
        queryRecords = this.findBySqlQuery(sql, pmSet);
        for (Scalar scalar : scalars) {
            queryRecords.addScalar(scalar.getFieldName(), scalar.getType());
        }
        queryRecords.setCacheable(usingCache());
        queryRecords.setResultTransformer(Transformers.aliasToBean(clazz));
        return queryRecords.list();
    }

    /**
     * <p>
     * Description: 根据构造基于SQL的查询条件进行翻页查询
     * filter.getQueryMap是查询条件，filter.getPageInfo()是翻页信息
     * 
     * <pre>
     * Scalar[] scalars = new Scalar[] { new Scalar(&quot;id&quot;, Hibernate.STRING), new Scalar(&quot;employeeName&quot;, Hibernate.STRING) };
     * </pre>
     * 
     * </p>
     * 
     * @param sql 查询字符串
     * @param pmSet 带查询条件的翻页信息
     * @param scalars 结果集映射数组
     * @param clazz 结果集往哪个类上进行映射
     * @return 查询结果
     * @throws BaseDaoException Dao异常
     */
    @SuppressWarnings("rawtypes")
    public PageModel findPagesListBySql(String sql, Set<ParamterDto> pmSet, int start, int limit, Scalar[] scalars,
                                        Class clazz) throws BaseDaoException {
        String obj = this.findBySqlQuery("select count(*) from (" + sql + ") t0", pmSet).uniqueResult()
                .toString();
        int count=Integer.parseInt(obj);
        PageModel pageModel = new PageModel();
        if(count>0){
            pageModel.setTotalRecords(count);
        }else{
            SQLQuery queryRecords;
            queryRecords = this.findBySqlQuery(sql, pmSet);
            for (Scalar scalar : scalars) {
                queryRecords.addScalar(scalar.getFieldName(), scalar.getType());
            }
            queryRecords.setCacheable(usingCache());
            queryRecords.setResultTransformer(Transformers.aliasToBean(clazz));
            List resultList;
            resultList = queryRecords.setFirstResult(start).setMaxResults(limit)
                    .list();
            pageModel.setDatas(resultList);
        }
        return pageModel;
    }

    /**
     * 
     * <p>
     * Description: 通过HQL查询列表，防止SQL注入问题，不翻页，满足条件就查出
     * </p>
     * 
     * @param queryString HQL字符串
     * @param pmSet 参数列表
     * @return model对象列表
     * @throws BaseDaoException 数据访问异常
     */
    @SuppressWarnings("rawtypes")
    public List findEntityListByHql(String queryString, Set<ParamterDto> pmSet)
            throws BaseDaoException {
        return findByQuery(queryString, pmSet).list();
    }

    /**
     * <p>
     * Description: 根据HQL进行进行翻页查询
     * filter.getQueryMap是查询条件，filter.getPageInfo()是翻页信息
     * </p>
     * 
     * @param countHql 查询总记录树的hql
     * @param recordHql 查询记录的hql
     * @param pmSet 参数
     * @param start 开始数
     * @param limit 参数
     * @return 查询结果
     * @throws BaseDaoException Dao异常
     */
    @SuppressWarnings("rawtypes")
    public PageModel findPagesListByHql(String countHql, String recordHql, Set<ParamterDto> pmSet, int start, int limit) throws BaseDaoException {
        int totalCount;
        totalCount = Integer.parseInt(this.findByQuery(countHql, pmSet).uniqueResult().toString());
        PageModel pageModel = new PageModel();
        if(totalCount>0){
            pageModel.setTotalRecords(totalCount);
        }else{
            Query queryRecords;
            queryRecords = this.findByQuery(recordHql, pmSet);
            queryRecords.setCacheable(usingCache());
            queryRecords.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
            List resultList;
            resultList = queryRecords.setFirstResult(start).setMaxResults(limit)
                    .list(); 
            pageModel.setDatas(resultList);
        }
        return pageModel;
    }

    /**
     * <p>
     * Description: 根据HQL进行进行翻页查询 ，查询结果Map形式,前台直接转json，方便多表联查
     * filter.getQueryMap是查询条件，filter.getPageInfo()是翻页信息
     * </p>
     * 
     * @param countHql 查询总记录树的hql
     * @param recordHql 查询记录的hql
     * @param pmSet 参数
     * @param start 开始数
     * @param limit 参数
     * @throws BaseDaoException Dao异常
     */
    @SuppressWarnings("unchecked")
    public PageModel findPagesMapByHql(String countHql, String recordHql, Set<ParamterDto> pmSet, int start, int limit)
            throws BaseDaoException {
        int totalCount;
        totalCount = Integer.parseInt(this.findByQuery(countHql, pmSet).uniqueResult().toString());
        PageModel pageModel = new PageModel();
        if(totalCount>0){
            pageModel.setTotalRecords(totalCount);
        }else{
            Query queryRecords;
            queryRecords = this.findByQuery(recordHql, pmSet);
            queryRecords.setCacheable(usingCache());
            queryRecords.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
            List<Map<String, Object>> resultMap;
            resultMap = (List<Map<String, Object>>) queryRecords.setFirstResult(start)
                    .setMaxResults(limit).list();
            pageModel.setDatas(resultMap);
        }
        return pageModel;
    }

    /**
     * 
     * <p>
     * Description: 处理paramOperate为EQ的参数
     * </p>
     * 
     * @param query
     * @param pmSet
     */
    private void setExecuteQueryParams(Query query, Set<ParamterDto> pmSet) {
        if (pmSet != null && !pmSet.isEmpty()) {
            for (ParamterDto pm : pmSet) {
                if (pm.getParamOperate() == Condition.EQ) {
                    query.setParameter(pm.getParamCode(), pm.getParamValue());
                }
            }
        }
    }

    /**
     * 
     * <p>
     * Description: 处理参数信息
     * </p>
     * 
     * @param query
     * @param pmSet
     */
    private void setFindQueryParams(Query query, Set<ParamterDto> pmSet) {
        if (pmSet != null && !pmSet.isEmpty()) {
            for (ParamterDto pm : pmSet) {
                switch (pm.getParamOperate()) {
                case LIKE:
                    if (pm.getParamValue() != null) {
                        query.setParameter(pm.getParamCode(), "%" + pm.getParamValue() + "%");
                    }
                    break;
                case JUST:
                case IN:
                case EQ:
                case GE:
                case LE:
                case GT:
                case LT:
                    if (pm.getParamValue() != null) {
                        query.setParameter(pm.getParamCode(), pm.getParamValue());
                    }
                    break;
                }
            }
        }
    }
}
