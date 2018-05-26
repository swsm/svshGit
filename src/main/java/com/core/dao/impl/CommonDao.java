/**
 * CommonDao.java
 * Created at 2015-12-01
 */
package com.core.dao.impl;

import com.core.dao.ICommonDao;
import com.core.exception.BaseDaoException;
import com.core.tools.Filter;
import com.core.tools.PageInfo;
import com.core.tools.Scalar;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.transform.Transformers;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 
 * <p>ClassName: CommonDao</p>
 * <p>Description: 数据访问的简单实现，这里封装了关于数据库的一些常用查询或更新操作，主要是基于SQL的</p>
 * <p>Author: zhang</p>
 * <p>Date: 2015-12-01</p>
 */
public class CommonDao extends BaseHibernateDao implements ICommonDao {
    
    /**
     * <p>Description: 是否使用缓存</p>
     * @return true or false
     */
    protected boolean usingCache() {
        return false;
    }
    
    /**
     * 
     * <p>Description: 通过SQL执行更新/插入/删除操作，参数需要用:变量名传递进来，如:employeeName</p>
     * @param queryString SQL字符串
     * @param queryMap 参数列表
     * @throws BaseDaoException 数据访问异常
     */
    public void updateBySqlQuery(String queryString, Map<String, com.core.tools.Query> queryMap) throws BaseDaoException {
        SQLQuery query;
        query = this.getSessionFactory().getCurrentSession().createSQLQuery(queryString);
        if (queryMap != null && queryMap.size() > 0 ) {
            for (String queryField : queryMap.keySet()) {
                if(queryMap.get(queryField) != null) {
                    if(queryMap.get(queryField).getValue() !=null ) {
                        query.setParameter(queryField, queryMap.get(queryField).getValue());
                    }
                }
            }
        }

        query.executeUpdate();
    }


    /**
     *
     * <p>Description: 通过SQL执行更新/插入/删除操作，参数需要用:变量名传递进来，如:employeeName</p>
     * @param queryString SQL字符串
     * @param queryMap 参数列表
     * @throws BaseDaoException 数据访问异常
     */
    public void executeSql(String queryString, Map<String, Object> queryMap) throws BaseDaoException {
        SQLQuery query;
        query = this.getSessionFactory().getCurrentSession().createSQLQuery(queryString);
        if (queryMap != null && queryMap.size() > 0 ) {
            for (String queryField : queryMap.keySet()) {
                if(queryMap.get(queryField) != null) {
                    if(queryMap.get(queryField) !=null ) {
                        query.setParameter(queryField, queryMap.get(queryField));
                    }
                }
            }
        }
        query.executeUpdate();
    }

    /**
     *
     * <p>Description: 通过HQL执行更新/插入/删除操作，参数需要用:变量名传递进来，如:employeeName</p>
     * @param queryString HQL字符串
     * @param queryMap 参数列表
     * @throws BaseDaoException 数据访问异常
     */
    public void updateByQuery(String queryString, Map<String, com.core.tools.Query> queryMap) throws BaseDaoException {
        Query query;
        query = this.getSessionFactory().getCurrentSession().createQuery(queryString);
        if (queryMap != null && queryMap.size() > 0 ) {
            for (String queryField : queryMap.keySet()) {
                if(queryMap.get(queryField) != null) {
                    if(queryMap.get(queryField).getValue() !=null ) {
                        query.setParameter(queryField, queryMap.get(queryField).getValue());
                    }
                }
            }
        }

        query.executeUpdate();
    }


    /**
     *
     * <p>Description: 通过HQL执行更新/插入/删除操作，参数需要用:变量名传递进来，如:employeeName</p>
     * @param queryString HQL字符串
     * @param paramMap 参数列表
     * @throws BaseDaoException 数据访问异常
     */
    public void executeHql(String queryString, Map<String, Object> paramMap) throws BaseDaoException {
        Query query;
        query = this.getSessionFactory().getCurrentSession().createQuery(queryString);
        if (paramMap != null && paramMap.size() > 0 ) {
            for (String queryField : paramMap.keySet()) {
                if(paramMap.get(queryField) != null) {
                    if(paramMap.get(queryField) !=null ) {
                        query.setParameter(queryField, paramMap.get(queryField));
                    }
                }
            }
        }

        query.executeUpdate();
    }

    /**
     *
     * <p>Description: 构造一个不带查询条件的HQL查询，防止SQL注入问题
     *  对于要first,max,fetchsize,cache,cacheRegion等诸多设置的函数,可以在返回Query后自行设
     * <pre>
     *      dao.getQuery(hql).setMaxResult(100).setCacheable(true).list();
     * </pre>
     * </p>
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
     * <p>Description: 构造一个带查询条件的HQL查询，防止SQL注入问题
     *  对于要first,max,fetchsize,cache,cacheRegion等诸多设置的函数,可以在返回Query后自行设
     * <pre>
     *      dao.getQuery(hql).setMaxResult(100).setCacheable(true).list();
     * </pre>
     * </p>
     * @param queryString HQL字符串
     * @param queryMap 参数列表
     * @return model对象列表
     * @throws BaseDaoException 数据访问异常
     */
    public Query findByQuery(String queryString, Map<String, com.core.tools.Query> queryMap) throws BaseDaoException {
        Query query;
        query = this.getSessionFactory().getCurrentSession().createQuery(queryString);
        query.setCacheable(usingCache());
        if (queryMap != null && queryMap.size() > 0 ) {
            for (String queryField : queryMap.keySet()) {
                if(queryMap.get(queryField) != null) {
                    switch (queryMap.get(queryField).getCondition()) {
                        case LIKE:
                            if(queryMap.get(queryField).getValue() !=null ) {
                                query.setParameter(queryField, "%" + queryMap.get(queryField).getValue() + "%");
                            }
                            break;
                        case IN:
                            Object value=queryMap.get(queryField).getValue();
                            if(value !=null ) {
                                if(value instanceof Collection){//集合类
                                    query.setParameterList(queryField, (Collection) value);
                                }else if(value.getClass().isArray()){//数组
                                    List listObject= Arrays.asList((Object[]) value);
                                    query.setParameterList(queryField, listObject);

                                }
                            }
                            break;
                        case JUST:
                        case EQ:
                        case GE:
                        case LE:
                        case GT:
                        case LT:
                            if(queryMap.get(queryField).getValue() !=null ) {
                                query.setParameter(queryField, queryMap.get(queryField).getValue());
                            }
                            break;
                    }
                }
            }
        }
        return query;
    }

    /**
     *
     * <p>Description: 构造基于SQL的查询，不带查询条件，防止SQL注入问题
     *  对于要first,max,fetchsize,cache,cacheRegion等诸多设置的函数,可以在返回Query后自行设
     * <pre>
     *      dao.findBySqlQuery(sql).setMaxResult(100).setCacheable(true).list();
     * </pre>
     * </p>
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
     * <p>Description: 狗仔一个基于SQL的查询，带查询条件，防止SQL注入问题
     *  对于要first,max,fetchsize,cache,cacheRegion等诸多设置的函数,可以在返回Query后自行设
     * <pre>
     *      dao.findBySqlQuery(sql).setMaxResult(100).setCacheable(true).list();
     * </pre>
     * </p>
     * @param queryString SQL字符串
     * @param queryMap 参数列表
     * @return model对象列表
     * @throws BaseDaoException 数据访问异常
     */
    public SQLQuery findBySqlQuery(String queryString, Map<String, com.core.tools.Query> queryMap) throws BaseDaoException {
        SQLQuery query;
        query = this.getSessionFactory().getCurrentSession().createSQLQuery(queryString);
        query.setCacheable(usingCache());
        if (queryMap != null && queryMap.size() > 0 ) {
            for (String queryField : queryMap.keySet()) {
                if(queryMap.get(queryField) != null) {
                    switch (queryMap.get(queryField).getCondition()) {
                        case LIKE:
                            if(queryMap.get(queryField).getValue() !=null ) {
                                //16-12-01 查询验证大小写 liujie
                                query.setParameter(queryField, "%" + queryMap.get(queryField).getValue() + "%");
                            }
                            break;
                        case JUST:
                        case IN:
                        case EQ:
                        case GE:
                        case LE:
                        case GT:
                        case LT:
                            if(queryMap.get(queryField).getValue() !=null ) {
                                query.setParameter(queryField, queryMap.get(queryField).getValue());
                            }
                            break;
                   
                    }
                }
            }
        }
        return query;
    }

   
    
    /**
     * <p>Description: 根据构造基于SQL的查询条件进行不翻页的查询，返回满足条件的全部记录
     * <pre>
     *          Scalar[] scalars = new Scalar[]{
     *           new Scalar("id", Hibernate.STRING),
     *           new Scalar("employeeName", Hibernate.STRING)
     *          };
     * </pre>
     * </p>
     * @param sql 查询字符串
     * @param queryMap 查询条件
     * @param scalars 结果集映射数组
     * @param clazz 结果集往哪个类上进行映射
     * @return 查询结果
     * @throws BaseDaoException Dao异常
     */
    @SuppressWarnings("unchecked")
    public List findEntityListBySql(String sql, Map<String, com.core.tools.Query> queryMap, Scalar[] scalars, Class clazz) throws BaseDaoException {
        
        SQLQuery queryRecords;
        queryRecords = this.findBySqlQuery(sql, queryMap);
        
        for(Scalar scalar:scalars){
            queryRecords.addScalar(scalar.getFieldName(), scalar.getType());
        }
        
        queryRecords.setCacheable(usingCache());
        queryRecords.setResultTransformer(Transformers.aliasToBean(clazz));
        List resultList;
        resultList = queryRecords.setFirstResult(PageInfo.START).setMaxResults(PageInfo.LIMIT).list();
        return resultList;
    }
    
    /**
     * <p>Description: 
     * 根据构造基于SQL的查询条件进行翻页查询
     * filter.getQueryMap是查询条件，filter.getPageInfo()是翻页信息
     * <pre>
     *          Scalar[] scalars = new Scalar[]{
     *           new Scalar("id", Hibernate.STRING),
     *           new Scalar("employeeName", Hibernate.STRING)
     *          };
     * </pre>
     * </p>
     * @param sql 查询字符串
     * @param filter 带查询条件的翻页信息 
     * @param scalars 结果集映射数组
     * @param clazz 结果集往哪个类上进行映射
     * @return 查询结果
     * @throws BaseDaoException Dao异常
     */
    @SuppressWarnings("unchecked")
    public List findPagesListBySql(String sql, Filter filter, Scalar[] scalars, Class clazz) throws BaseDaoException {
        String obj  = this.findBySqlQuery("select count(*) from (" + sql +") t0", filter.getQueryMap()).uniqueResult().toString();
        filter.getPageInfo().count = Long.parseLong(obj);
        
        SQLQuery queryRecords;
        queryRecords = this.findBySqlQuery(sql, filter.getQueryMap());
        
        for(Scalar scalar:scalars){
            queryRecords.addScalar(scalar.getFieldName(), scalar.getType());
        }
        
        queryRecords.setCacheable(usingCache());
        queryRecords.setResultTransformer(Transformers.aliasToBean(clazz));
        List resultList;
        resultList = queryRecords.setFirstResult(filter.getPageInfo().start).setMaxResults(filter.getPageInfo().limit).list();
        return resultList;
    }

    /**
     * 
     * <p>Description: 通过HQL查询列表，防止SQL注入问题，不翻页，满足条件就查出
     * </p>
     * @param queryString HQL字符串
     * @param queryMap 参数列表
     * @return model对象列表
     * @throws BaseDaoException 数据访问异常
     */
    @SuppressWarnings("unchecked")
    public List findEntityListByHql(String queryString, Map<String, com.core.tools.Query> queryMap) throws BaseDaoException {
        return findByQuery(queryString, queryMap).list();
    }

    /**
     * <p>
     * Description: 根据HQL进行进行翻页查询 
     * filter.getQueryMap是查询条件，filter.getPageInfo()是翻页信息
     * </p>
     * @param countHql 查询总记录树的hql
     * @param recordHql 查询记录的hql
     * @param filter 带查询条件的翻页信息
     * @return 查询结果
     * @throws BaseDaoException Dao异常
     */
    @SuppressWarnings("unchecked")
    public List findPagesListByHql(String countHql, String recordHql, Filter filter) throws BaseDaoException {
        long totalCount;
        totalCount  = ((Long)this.findByQuery(countHql, filter.getQueryMap()).uniqueResult()).longValue();
        filter.getPageInfo().count = totalCount;
        
        Query queryRecords;
        queryRecords = this.findByQuery(recordHql, filter.getQueryMap());
                
        queryRecords.setCacheable(usingCache());
        queryRecords.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
        List resultList;
        resultList = queryRecords.setFirstResult(filter.getPageInfo().start).setMaxResults(filter.getPageInfo().limit).list();
        return resultList;
    }
    
    /**
     * <p>
     * Description: 根据HQL进行进行翻页查询 ，查询结果Map形式,前台直接转json，方便多表联查
     * filter.getQueryMap是查询条件，filter.getPageInfo()是翻页信息
     * </p>
     * @param countHql 查询总记录树的hql
     * @param recordHql 查询记录的hql
     * @param filter 带查询条件的翻页信息
     * @return Map的list
     * @throws BaseDaoException Dao异常
     */
    public List<Map<String, Object>> findPagesMapByHql(String countHql, String recordHql, Filter filter)
            throws BaseDaoException {
        long totalCount;
        totalCount = ((Long) this.findByQuery(countHql, filter.getQueryMap()).uniqueResult()).longValue();
        filter.getPageInfo().count = totalCount;

        Query queryRecords;
        queryRecords = this.findByQuery(recordHql, filter.getQueryMap());

        queryRecords.setCacheable(usingCache());
        queryRecords.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> resultMap;
        resultMap = (List<Map<String, Object>>) queryRecords.setFirstResult(filter.getPageInfo().start).setMaxResults(filter.getPageInfo().limit).list();
        return resultMap;
    }
}
