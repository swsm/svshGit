package com.core.dao;

import com.core.exception.BaseDaoException;
import com.core.tools.Filter;
import com.core.tools.Scalar;
import org.hibernate.Query;
import org.hibernate.SQLQuery;

import java.util.List;
import java.util.Map;

public interface ICommonDao {

    /**
     * 
     * <p>Description: 通过SQL执行更新/插入/删除操作，参数需要用:变量名传递进来，如:employeeName</p>
     * @param queryString SQL字符串
     * @param queryMap 参数列表
     * @throws BaseDaoException 数据访问异常
     */
    public void updateBySqlQuery(String queryString, Map<String, com.core.tools.Query> queryMap) throws BaseDaoException;
    
    /**
     * 
     * <p>Description: 通过SQL执行更新/插入/删除操作，参数需要用:变量名传递进来，如:employeeName</p>
     * @param queryString SQL字符串
     * @param queryMap 参数列表
     * @throws BaseDaoException 数据访问异常
     */
    public void executeSql(String queryString, Map<String, Object> queryMap) throws BaseDaoException;
    
    /**
     * 
     * <p>Description: 通过HQL执行更新/插入/删除操作，参数需要用:变量名传递进来，如:employeeName</p>
     * @param queryString HQL字符串
     * @param queryMap 参数列表
     * @throws BaseDaoException 数据访问异常
     */
    public void updateByQuery(String queryString, Map<String, com.core.tools.Query> queryMap) throws BaseDaoException;

    
    /**
     * 
     * <p>Description: 通过HQL执行更新/插入/删除操作，参数需要用:变量名传递进来，如:employeeName</p>
     * @param queryString HQL字符串
     * @param paramMap 参数列表
     * @throws BaseDaoException 数据访问异常
     */
    public void executeHql(String queryString, Map<String, Object> paramMap) throws BaseDaoException;
 
    /**
     * 
     * <p>Description: 构造一个不带查询条件的HQL查询，防止SQL注入问题
     *  对于要first,max,fetchsize,cache,cacheRegion等诸多设置的函数,可以在返回Query后自行设
     * </p>
     * @param queryString HQL字符串
     * @return model对象列表
     * @throws BaseDaoException 数据访问异常
     */
    public Query findByQuery(String queryString) throws BaseDaoException;
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
    public Query findByQuery(String queryString, Map<String, com.core.tools.Query> queryMap) throws BaseDaoException;
    
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
    public SQLQuery findBySqlQuery(String queryString) throws BaseDaoException;
    
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
    public SQLQuery findBySqlQuery(String queryString, Map<String, com.core.tools.Query> queryMap) throws BaseDaoException;
   
    
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
    public List findEntityListBySql(String sql, Map<String, com.core.tools.Query> queryMap, Scalar[] scalars, Class clazz) throws BaseDaoException;
    
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
    public List findPagesListBySql(String sql, Filter filter, Scalar[] scalars, Class clazz) throws BaseDaoException ;
    /**
     * 
     * <p>Description: 通过HQL查询列表，防止SQL注入问题，不翻页，满足条件就查出
     * </p>
     * @param queryString HQL字符串
     * @param queryMap 参数列表
     * @return model对象列表
     * @throws BaseDaoException 数据访问异常
     */
    public List findEntityListByHql(String queryString, Map<String, com.core.tools.Query> queryMap) throws BaseDaoException;

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
    public List findPagesListByHql(String countHql, String recordHql, Filter filter) throws BaseDaoException ;
    
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
            throws BaseDaoException;


}

