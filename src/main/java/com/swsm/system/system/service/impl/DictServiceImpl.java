package com.swsm.system.system.service.impl;

import com.core.dao.impl.BaseDaoImpl;
import com.core.service.impl.EntityServiceImpl;
import com.core.tools.Filter;
import com.swsm.system.system.dao.impl.DictDaoImpl;
import com.swsm.system.system.model.Dict;
import com.swsm.system.system.service.IDictService;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * <p>ClassName: DictService</p>
 * <p>Description: 字典明细的业务处理类</p>
 */
@Service("dictService")
public class DictServiceImpl  extends EntityServiceImpl<Dict> implements IDictService {

    /**
     * 字典明细的数据库存取对象
     */
    @Autowired
    @Qualifier("dictDao")
    private DictDaoImpl dictDao;
    
    @Autowired
    @Qualifier("dictDao")
    @Override
    public void setBaseDao(BaseDaoImpl<Dict, String> baseDaoImpl) {
        this.baseDaoImpl = baseDaoImpl;
    }
    
    /**
     * <p>Description: 查询字典下所有的节点值</p>
     * @param parentKey 字典标识
     * @return 字典清单列表
     */
    public List<Dict> findAllDicts(String parentKey) {
        DetachedCriteria criteria;
        criteria = DetachedCriteria.forClass(Dict.class);
        criteria.add(Property.forName("parentKey").eq(parentKey));
        criteria.add(Property.forName("delFlag").eq("0"));
        criteria.addOrder(Order.asc("dictSort"));
        return this.dictDao.findByCriteria(criteria);
    }

    /**
     * <p>Description: 检查字典是否重复</p>
     * @param filter 查询条件
     * @param dictId 主键
     * @param parentKey 所属字典的索引值
     * @param dictKey 字典键
     * @param dictValue 字典显示值
     * @throws Exception 异常信息
     * @return 重复了的字典
     */
    public Dict checkDict(Filter filter, String dictId, String parentKey, String dictKey, String dictValue)
            throws Exception {
        //创建查询对象
        DetachedCriteria criteria;
        criteria = DetachedCriteria.forClass(Dict.class);
        criteria.add(Property.forName("parentKey").eq(parentKey));

        if (StringUtils.isNotEmpty(dictId)) {
            criteria.add(Property.forName("id").ne(dictId));
        }

        Disjunction dis;
        dis = Restrictions.disjunction();
        dis.add(Restrictions.eq("dictKey", dictKey));
        dis.add(Restrictions.eq("dictValue", dictValue));
        criteria.add(dis);

        //执行查询
        List<Dict> dictList;
        dictList = this.dictDao.findByCriteria(criteria, filter.getPageInfo());
        return dictList.isEmpty() ? null : dictList.get(0);
    }

    /**
     * <p>Description: 根据字典键和索引代码查询对应的值</p>
     * @param filter 查询条件
     * @return 字典显示值
     */
    public String findDictValueByKey(Filter filter) {
        //创建查询对象
        DetachedCriteria criteria;
        criteria = DetachedCriteria.forClass(Dict.class);
        //封装过滤器、传入查询条件
        doFilter(filter, criteria);
        //执行查询
        List<Dict> dictList;
        dictList = this.dictDao.findByCriteria(criteria, filter.getPageInfo());
        return dictList.isEmpty() ? null : dictList.get(0).getDictValue();
    }
    
    /**
     * <p>Description: 获取某一字典索引下的所有字典值</p>
     * @param filter 过滤器
     * @param dictClass 字典类 
     * @return 字典列表
     */
    public List<Dict> getDictForGrid(Filter filter, Class<Dict> dictClass) {
        List<String> params;
        params = new ArrayList<>();
        if (checkValid("parentKey", filter.getQueryMap())) {
            params.add("parentKey");
        }
        // 执行查询
        List<Dict> list;
        list = this.dictDao.getDictForGrid(filter, params);
        return list;
    }
    
    /**
     * <p>Description: 保存或更新字典</p>
     * @param objList 需要操作的字典对象列表
     * @param userName 登录人用户名
     * @return String 操作结果
     */
    public String saveOrUpdateDict(List<Dict> objList, String userName) {
        for (Dict dict : objList) {
            if (StringUtils.isEmpty(dict.getId())) {
                dict.setDelFlag(DEL_FALSE);
                dict.setCreateDate(new Date());
                dict.setCreateUser(userName);
                this.baseDaoImpl.save(dict);
            } else {
                //更新
                dict.setDelFlag(DEL_FALSE);
                dict.setUpdateDate(new Date());
                dict.setUpdateUser(userName);
                this.baseDaoImpl.update(dict);
            }
        }
        return "success";
    }
    
    /**
     * <p>Description: 删除字典</p>
     * @param ids 字典索引id 数组
     * @param userName 操作人
     * @return 操作结果
     */
    public String deleteDict(String ids, String userName) {
        for (String id : ids.split(",")) {
            Dict dict;
            dict = (Dict) this.baseDaoImpl.findUniqueByProperty(Dict.class, "id", id);
            dict.setDelFlag(DEL_TRUE);
            dict.setUpdateDate(new Date());
            dict.setUpdateUser(userName);
            this.baseDaoImpl.update(dict);
        }
        return "success";
    }
    
    @Override
    public String getDictValueByKey(String parentKey, String dictKey) {
        List<Dict> list;
        list = this.dictDao.getDictValueByKey(parentKey, dictKey);
        if (!list.isEmpty()) {
            return list.get(0).getDictValue();
        }
        return null;
    }
    
    @Override
    public String getDictKeyByValue(String parentKey, String dictValue) {
        List<Dict> list;
        list = this.dictDao.getDictKeyByValue(parentKey, dictValue);
        if (!list.isEmpty()) {
            return list.get(0).getDictKey();
        }
        return null;
    }
    
    @Override
    public List<Dict> getDictListyByParentKey(String parentKey) {
        List<Dict> list;
        list = this.dictDao.getDictListyByParentKey(parentKey);
        if (!list.isEmpty()) {
            return list;
        }
        return Collections.emptyList();
    }
}
