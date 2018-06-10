package com.swsm.system.service.impl;


import com.core.dao.impl.BaseDaoImpl;
import com.core.service.impl.EntityServiceImpl;
import com.core.tools.Filter;
import com.swsm.system.dao.impl.DictDaoImpl;
import com.swsm.system.dao.impl.DictIdxDaoImpl;
import com.swsm.system.model.Dict;
import com.swsm.system.model.DictIdx;
import com.swsm.system.service.IDictIdxService;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>ClassName: DictIdxService</p>
 * <p>Description: 字典索引的业务处理类</p>
 */
@Service("dictIdxService")
public class DictIdxServiceImpl  extends EntityServiceImpl<DictIdx> implements IDictIdxService {

    /**
     * 字典索引的数据库存取类
     */
    @Autowired
    @Qualifier("dictIdxDao")
    private DictIdxDaoImpl dictIdxDao;
    
    /**
     * 字典的数据库存取类
     */
    @Autowired
    @Qualifier("dictDao")
    private DictDaoImpl dictDao;
    
    @Autowired
    @Qualifier("dictIdxDao")
    @Override
    public void setBaseDao(BaseDaoImpl<DictIdx, String> baseDaoImpl) {
        this.baseDaoImpl = baseDaoImpl;
    }
    
    /**
     * <p>Description: 检查字典索引是否重复</p>
     * @param filter 查询条件
     * @param dictId 主键
     * @param dictKey 字典标识
     * @param dictName 字典名称
     * @return 重复了的字典
     * @throws Exception 异常信息
     */
    public DictIdx checkDictIdx(Filter filter, String dictId, String dictKey, String dictName) throws Exception {
        //创建查询对象
        DetachedCriteria criteria;
        criteria = DetachedCriteria.forClass(DictIdx.class);

        if (StringUtils.isNotEmpty(dictId)) {
            criteria.add(Property.forName("id").ne(dictId));
        }

        Disjunction dis;
        dis = Restrictions.disjunction();
        dis.add(Restrictions.eq("dictKey", dictKey));
        dis.add(Restrictions.eq("dictName", dictName));
        criteria.add(dis);

        //执行查询
        List<DictIdx> dictIdxList;
        dictIdxList = this.dictIdxDao.findByCriteria(criteria, filter.getPageInfo());
        return dictIdxList.isEmpty() ? null : dictIdxList.get(0);
    }

    /**
     * <p>Description: 查询字典索引</p>
     * @param filter 过滤器
     * @param dictIdx 字典索引
     * @return 字典索引列表
     */
    public List<DictIdx> getDictIdxForGrid(Filter filter, Class<DictIdx> dictIdx) {
        List<String> params;
        params = new ArrayList<>();
        if (checkValid("dictName", filter.getQueryMap())) {
            params.add("dictName");
        }
        // 执行查询
        List<DictIdx> list;
        list = this.dictDao.getDictIdxForGrid(filter, params);
        return list;
    }
    

    /**
     * <p>Description: 保存或更新字典索引</p>
     * @param objList 需要操作的字典索引对象列表
     * @param userName 登录人用户名
     * @return String 操作结果
     */
    public String saveOrUpdateDictIndex(List<DictIdx> objList, String userName) {
        for (DictIdx dictIndex : objList) {
            if (StringUtils.isEmpty(dictIndex.getId())) {
                dictIndex.setDelFlag(DEL_FALSE);
                dictIndex.setCreateDate(new Date());
                dictIndex.setCreateUser(userName);
                this.baseDaoImpl.save(dictIndex);
            } else {
                //更新
                dictIndex.setDelFlag(DEL_FALSE);
                dictIndex.setUpdateDate(new Date());
                dictIndex.setUpdateUser(userName);
                this.baseDaoImpl.update(dictIndex);
            }
        }
        return "success";
    }

    /**
     * <p>Description: 删除字典索引</p>
     * @param ids 字典索引id 数组
     * @param username 操作人
     * @return 操作结果
     */
    public String deleteDictIndex(String ids, String username) {
        for (String id : ids.split(",")) {
            DictIdx dictIdx;
            dictIdx = (DictIdx) this.baseDaoImpl.findUniqueByProperty(DictIdx.class, "id", id);
            //将字典索引下的所有字典也假删除
            List<Dict> dictList;
            dictList = this.dictDao.deleteDictIndex(dictIdx);
            for (Dict dict : dictList) {
                dict.setDelFlag(DEL_TRUE);
                dict.setUpdateDate(new Date());
                dict.setUpdateUser(username);
                this.dictDao.update(dict);
            }
            dictIdx.setDelFlag(DEL_TRUE);
            dictIdx.setUpdateDate(new Date());
            dictIdx.setUpdateUser(username);
            this.baseDaoImpl.update(dictIdx);
        }
        return "success";
    }

    @Override
    public String saveOrUpdateDictIndexDemo(DictIdx dictIndex, String oldDictKey, String newDictKey, String username) {
        List<DictIdx> dictIdxList;
        if (dictIndex.getId() == null || dictIndex.getId().isEmpty()) {
            //新增 _前缀的id为新增的记录
            dictIdxList = this.dictIdxDao.saveOrUpdateDictIndexDemo(newDictKey);
            if (dictIdxList.isEmpty()) {
                dictIndex.setDictKey(newDictKey);
                dictIndex.setDelFlag(DEL_FALSE);
                dictIndex.setCreateDate(new Date());
                dictIndex.setCreateUser(username);
                this.baseDaoImpl.save(dictIndex);
            } else {
                return "dictKeyIsExist";
            }
        } else {
            //更新
            dictIdxList = this.dictIdxDao.saveOrUpdateDictIndexDemo(dictIndex, newDictKey);
            if (dictIdxList.isEmpty()) {
                //将原先属于此字典索引的字典也同步修改
                List<Dict> dictList;
                dictList = this.dictDao.findByProperty(Dict.class, "parentKey", oldDictKey);
                for (Dict dict : dictList) {
                    dict.setParentKey(newDictKey);
                    dict.setUpdateDate(new Date());
                    dict.setUpdateUser(username);
                    this.dictDao.update(dict);
                }
                dictIndex.setDictKey(newDictKey);
                dictIndex.setDelFlag(DEL_FALSE);
                dictIndex.setUpdateDate(new Date());
                dictIndex.setUpdateUser(username);
                this.baseDaoImpl.update(dictIndex);
            } else {
                return "dictKeyIsExist";
            }
        }
        return "success";
    }

}
