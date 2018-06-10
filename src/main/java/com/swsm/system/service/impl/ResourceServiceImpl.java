package com.swsm.system.service.impl;

import com.core.dao.impl.BaseDaoImpl;
import com.core.service.impl.EntityServiceImpl;
import com.core.tools.Filter;
import com.swsm.system.dao.impl.ResourceDaoImpl;
import com.swsm.system.model.Resource;
import com.swsm.system.service.IResourceService;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * <p>ClassName: ResourceService</p>
 * <p>Description: 资源管理的业务处理</p>
 */
@Service("resourceService")
public class ResourceServiceImpl extends EntityServiceImpl<Resource> implements IResourceService {

    @Autowired
    @Qualifier("resourceDao")
    @Override
    public void setBaseDao(BaseDaoImpl<Resource, String> baseDaoImpl) {
        this.baseDaoImpl = baseDaoImpl;
    }
    /**
     * 实例化resourceDao
     * 
     */
    @Autowired
    @Qualifier("resourceDao")
    private ResourceDaoImpl resourceDao;
    
    /**
     * <p>Description: 取得特殊的排序字段集合，一般的话不放在这里</p>
     * @return 特殊的排序字段集合
     */
    protected Map<String, String> getOrderMap() {
        Map<String, String> orderMap;
        orderMap = new HashMap<>();
        orderMap.put("parentResName", "parentResource.resName");
        return orderMap;
    }
    
    /**
     * 
     * <p>Description: 条件查询</p>
     * @param filter 过滤器
     * @param clazz 实体类
     * @return 查询结果
     * @throws Exception 运行时异常
     */
    public List<Resource> queryPagedEntityList(Filter filter, Class<Resource> clazz) throws Exception {
        //创建查询对象
        DetachedCriteria criteria;
        criteria = DetachedCriteria.forClass(clazz);
        //封装过滤器、传入查询条件
        doFilter(filter, criteria);
        //过滤逻辑删除
        criteria.add(Property.forName("delFlag").eq("0"));
        //执行查询
        List<Resource> list;
        list = this.baseDaoImpl.findByCriteria(criteria, filter.getPageInfo());
        //将查询列表与分页信息转换为json对象送到前台
        return list;
    }
    
    
    /**
     * 启用或禁用资源
     * @param resources 资源列表
     * @param status 状态
     */
    public void updateResourceStatus(List<Resource> resources, String status) {
        for (Resource resource : resources) {
            Resource dbResource;
            dbResource = this.findEntity(Resource.class, resource.getId());
            dbResource.setEnabled(status);
            this.baseDaoImpl.update(dbResource);
        }
    }
    
    /**
     * 
     * <p>Description: 删除对象列表</p>
     * @param resources 需要删除的对象列表
     */
    public void realDeleteEntity(List<Resource> resources) {
        for (Resource resource : resources) {
            Resource dbResource;
            dbResource = this.findEntity(Resource.class, resource.getId());
            if (dbResource != null) {
                this.baseDaoImpl.delete(dbResource);
            }
        }
    }

    /**
     * <p>Description: 为角色选择资源时弹出的资源树窗口</p>
     * @param parentId 上级资源id
     * @param roleId 角色Id，用来判断是否默认选中某资源
     * @param flag 查询按钮标识
     * @return 资源树json字符串
     */
    public List<Resource> getResourceTreeByFilter(String parentId, String roleId, String flag) {
        //执行查询
        List<Resource> resourceList;
        resourceList = new ArrayList<>();
        SQLQuery query;
        query = this.resourceDao.getResourceTreeByFilter(parentId, flag);
        List<Resource> list;
        list = query.list();
        List<String> resIds;
        resIds = this.getResIdsByRoleId(roleId);
        for (Resource r:list) {
            boolean b;
            b = resIds.contains(r.getId());
            if (b) {
                r.setDbChecked("1");
            } else {
                r.setDbChecked("0");
            }
            
        }
        resourceList.addAll(list);
        return resourceList;
    }
    
    
    @SuppressWarnings({ "deprecation", "unchecked" })
    @Override
    public List<Resource> getResourceTree(String parentId, String roleId, String flag) {
        //执行查询
        List<Resource> resourceList;
        resourceList = new ArrayList<Resource>();
        StringBuilder sb = new StringBuilder();
        sb.append("select t0.PK_ID as id, t0.RES_NAME as resName,t0.PARENT_ID as parentIdStr,")
                .append("(select count(t2.PK_ID) from sys_resource t2 ") 
                .append("where t2.parent_id = t0.PK_ID and t2.enabled = '1' and t2.del_flag = '0') as childCount, ")
                .append("t0.RES_CODE as resCode, t0.RES_TYPE as resType ")
                .append("from SYS_RESOURCE t0 ")
                .append("where t0.del_flag ='0' and t0.enabled ='1' ");
        sb.append("order by nvl(t0.parent_id, 0),t0.res_order ");
        SQLQuery query = this.baseDaoImpl.findBySqlQuery(sb.toString());
        query.addScalar("id", StringType.INSTANCE);
        query.addScalar("parentIdStr", StringType.INSTANCE);
        query.addScalar("childCount", IntegerType.INSTANCE);
        query.addScalar("resName", StringType.INSTANCE);
        query.addScalar("resCode", StringType.INSTANCE);
        query.addScalar("resType", StringType.INSTANCE);
        query.setResultTransformer(CriteriaSpecification.ROOT_ENTITY).setCacheable(false).setResultTransformer(
                Transformers.aliasToBean(Resource.class));
        List<Resource> list = query.list();
        List<String> resIds;
        resIds = this.getResIdsByRoleId(roleId);
        for (Resource r : list) {
            if (resIds.contains(r.getId())) {
                r.setDbChecked("1");
            } else {
                r.setDbChecked("0");
            }
            
        }
        getChildResourceForTree(resourceList, list, null);
        return resourceList;
    }
    
    /**
     * 
     * <p>Description: 设置子资源 开始递归</p>
     * @param resourceList 结果list
     * @param list 原所有资源
     * @param parentId 顶级父资源parentId
     * @return 结果资源
     */
    private List<Resource> getChildResourceForTree(List<Resource> resourceList, List<Resource> list, String parentId) {
        for (int i = 0; i < list.size(); i++) {
            if ((parentId == null && list.get(i).getParentIdStr() == null) 
                    || (parentId != null && parentId.equals(list.get(i).getParentIdStr()))) {
                list.get(i).setChildren(getChildResSet(list.get(i).getId(), list));
                resourceList.add(list.get(i));
            }
        }
        return resourceList;
    }
    
    /**
     * 
     * <p>Description: 获取子资源set集合 递归</p>
     * @param parentId 父资源id
     * @param list 资源list
     * @return 子资源set集合
     */
    private Set<Resource> getChildResSet(String parentId, List<Resource> list) {
        Set<Resource> set = new HashSet<Resource>();
        for (Resource r : list) {
            if (parentId.equals(r.getParentIdStr())) {
                r.setChildren(getChildResSet(r.getId(), list));
                set.add(r);
            }
        }
        return set;
    }
    
    /**
     * <p>Description: TODO</p>
     * @param roleId roleId
     * @return List<String>
     */
    @SuppressWarnings("unchecked")
    private List<String> getResIdsByRoleId(String roleId) {
        SQLQuery query;
        query = this.resourceDao.getResIdsByRoleId(roleId);
        List<Map<String, Object>> list;
        list = query.list();
        List<String> rlt;
        rlt = new ArrayList<String>();
        for (Map<String, Object> o:list) {
            rlt.add(o.get("res_id").toString());
        }
        return rlt;
    }

    /**
     * <p>Description: 查询模块级资源，形成下拉框，便于过滤</p>
     * @param filter 查询条件
     * @return 模块级资源形成的json字符串
     */
    public List<Resource> getModulesByFilter(Filter filter) {
        //创建查询对象
        DetachedCriteria criteria;
        criteria = DetachedCriteria.forClass(Resource.class);
        //封装过滤器、传入查询条件
        doFilter(filter, criteria);
        //执行查询
        List<Resource> resourceList;
        resourceList = new ArrayList<>();
        resourceList.add(new Resource("root", "不限"));
        resourceList.addAll(this.resourceDao.findByCriteria(criteria, filter.getPageInfo()));
        
        return resourceList;
    }

    /**
     * <p>Description: 查询上级资源的下拉树形选择框</p>
     * @param resName 资源名称
     * @return 资源形成的json字符串
     */
    public List<Map<String, Object>> getResourceTreeForParentByFilter(String resName) {
        List<Resource> resList;
        resList = this.resourceDao.getResourceTreeForParentByFilter(resName);
        List<Resource> lists;
        lists = new ArrayList<>();
        if (StringUtils.isNotEmpty(resName)) {
            for (Resource res : resList) {
                lists.add(res);
                if (res.getParentResource() != null) {
                    this.getResourceByPid(res, lists);
                }
            }
        } else {
            lists =  resList;
        }
        Set<Resource> set;
        set = new LinkedHashSet<Resource>();
        set.addAll(lists);
        lists.clear();
        lists.addAll(set);
        List<Map<String, Object>> list;
        list = new ArrayList<Map<String, Object>>();
        for (Resource res : lists) {
            Map<String, Object> m;
            m = new HashMap<String, Object>();
            if (res.getParentResource() != null) {
                m.put("parentId", res.getParentResource().getId());
            } else {
                m.put("parentId", null);
            }
            m.put("id", res.getId());
            m.put("text", res.getResName());
            list.add(m);
        }
        return list;
    }
    
    /**
     * <p>Description: 查询上级资源</p>
     * @param resource 资源
     * @param lists 资源list集合
     */
    private void getResourceByPid(Resource resource, List<Resource> lists) {
        if (resource.getParentResource() != null) {
            Resource res;
            res = this.baseDaoImpl.getById(Resource.class, resource.getParentResource().getId());
            lists.add(res);
            this.getResourceByPid(res, lists);
        }
    }

    /**
     * <p>Description: 新增或修改资源</p>
     * @param objList 需要操作的资源对象列表
     * @param userName 登录人用户名
     */
    public void saveOrUpdateResource(List<Resource> objList, String userName) {
        for (Resource resource : objList) {
            if (StringUtils.isEmpty(resource.getId())) {
                //新增
                resource.setDelFlag(DEL_FALSE);
                resource.setCreateDate(new Date());
                resource.setCreateUser(userName);
                this.baseDaoImpl.save(resource);
            } else {
                //更新
                resource.setDelFlag(DEL_FALSE);
                resource.setUpdateDate(new Date());
                resource.setUpdateUser(userName);
                resource.setRoleList(this.baseDaoImpl.getById(Resource.class, resource.getId()).getRoleList());
                this.baseDaoImpl.getHibernateTemplate().merge(resource);
            }
        }
    }
    
    /**
     * <p>Description: 启用或禁用资源</p>
     * @param enabled 状态
     * @param ids 资源id数组
     */
    public void updateResourceEnabled(String enabled, String[] ids) {
        for (String id : ids) {
            Resource resource;
            resource = this.baseDaoImpl.getById(Resource.class, id);
            resource.setEnabled(enabled);
            this.baseDaoImpl.update(resource);
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> exportResourceList(Filter filter) {
        List<String> params;
        params = new ArrayList<>();
        if (checkValid("resName", filter.getQueryMap())) {
            params.add("resName");
        }
        if (checkValid("resType", filter.getQueryMap())) {
            params.add("resType");
        }
        List list;
        org.hibernate.Query query = null;
        //query = this.resourceDao.exportResourceList(filter, params);
        list = query.list();
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        return list;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> getResourceTreeForExport(String roleId) {
        List<Map<String, Object>> list;
        org.hibernate.Query query ;
        query = this.resourceDao.getResourceTreeForExport(roleId);
        list = query.list();
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        this.resetNumberList(list);
        return list;
    }


    /**
     * 
     * <p>
     * Description: 重置list序号问题（序号:1->1.1->1.1.1 来进行展示）
     * </p>
     * 
     * @param list list结果集
     */
    private void resetNumberList(List<Map<String, Object>> list) {
        int maxLevel;
        maxLevel = 1;
        int level;
        //进行保存每次的使用值
        Map<String, Integer> mapKey;
        mapKey = new HashMap<>();
        //进行初始化
        mapKey.put("1", 1); 
        list.get(0).put("xh", 1);
        for (int i = 1; i < list.size(); i++) {
            //表示存在
            level = Integer.parseInt(list.get(i).get("le").toString());
            //进行数据自动加以
            if (mapKey.containsKey(list.get(i).get("le").toString())) {
                
                if (level <= maxLevel) {
                    mapKey.put(String.valueOf(level), mapKey.get(String.valueOf(level)) + 1);
                    for (int j = (level + 1); j <= maxLevel; j++) {
                        mapKey.remove(String.valueOf(j));
                    }
                }
            } else {
                //表示不存在
                mapKey.put(list.get(i).get("le").toString(), 1);
                if (maxLevel < Integer.parseInt(list.get(i).get("le").toString())) {
                    maxLevel = Integer.parseInt(list.get(i).get("le").toString());
                }
            }
            String xh = null;
            //序号进行赋值
            for (int j =1; j <= level; j++) {
                if (level == j) {
                    if (xh == null) {
                        xh = mapKey.get(String.valueOf(j)) + "";
                    } else {
                        xh += mapKey.get(String.valueOf(j)) + "";
                    }
                } else {
                    if (xh == null) {
                        xh = mapKey.get(String.valueOf(j)) + ".";
                    } else {
                        xh += mapKey.get(String.valueOf(j)) + ".";  
                    }
                }
            }
            list.get(i).put("xh", xh);
        }

    }

}
