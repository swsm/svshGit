package com.swsm.system.system.service.impl;

import com.core.dao.impl.BaseDaoImpl;
import com.core.exception.BaseException;
import com.core.service.impl.EntityServiceImpl;
import com.swsm.system.constant.ActionConstants;
import com.swsm.system.system.dao.impl.OrganDaoImpl;
import com.swsm.system.system.model.Organ;
import com.swsm.system.system.service.IOrganService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * <p>ClassName: OrganService</p>
 * <p>Description: 机构管理的业务处理</p>
 */
@Service("organService")
public class OrganServiceImpl  extends EntityServiceImpl<Organ> implements IOrganService {
    @Autowired
    @Qualifier("organDao")
    @Override
    public void setBaseDao(BaseDaoImpl<Organ, String> baseDaoImpl) {
        this.baseDaoImpl = baseDaoImpl;
    }
    /**
     * organDao实例化
     */
    @Autowired
    @Qualifier("organDao")
    private OrganDaoImpl organDao;
    
    @Override
    protected Map<String, String> getOrderMap() {
        Map<String, String> orderMap;
        orderMap = new HashMap<>();
        orderMap.put("parentOrganName", "parentOrgan.organName");
        return orderMap;
    }


    @Override
    public List<Organ> findOrganByPanrentId(String parentId) throws BaseException {
        if (ActionConstants.ROOT_NODE_ID.equals(parentId)) {
            List<Organ> list;
            list = this.organDao.findOrganByPanrentId();
            return list;
        } else {
            List<Organ> list;
            list = this.organDao.findOrganByPanrentId(parentId);
            return list;
        }
    }

    /**
     * <p>Description: 获取所有子 部门</p>
     * @param childsOrgan 子部门
     * @return 子部门列表
     */
    public List<Organ> getChildsOrgan(Set<Organ> childsOrgan) {
        List<Organ> list;
        list = new ArrayList<Organ>();
        list.addAll(childsOrgan);
        for (Organ o : childsOrgan) {
            list.addAll(this.getChildsOrgan(o.getChildren()));
        }
        return list;
    }
    
    @Override
    public String deleteOrgan(String id, String username) throws BaseException {
        //1.子部门删除标记设为 true
        List<Organ> childOrganList;
        childOrganList = this.getChildsOrgan(
                this.baseDaoImpl.findByProperty(Organ.class, "id", id).get(0).getChildren());
        for (Organ o : childOrganList) {
            o.setUpdateDate(new Date());
            o.setUpdateUser(username);
            o.setDelFlag(DEL_TRUE);
        }
        
        //2.父部门删除标记设为 true
        Organ organ;
        organ = this.findEntity(Organ.class, id);
        organ.setUpdateDate(new Date());
        organ.setUpdateUser(username);
        organ.setDelFlag(DEL_TRUE);
        
        return "success";
    }

    @Override
    public String saveOrgan(Organ organ, String userName) {
        //验证机构编码的唯一性
        List<Organ> list;
        list = this.organDao.findOrgan(organ, userName);
        if (!list.isEmpty()) {
            return "organCodeExist";
        }
        organ.setCreateUser(userName);
        organ.setCreateDate(new Date());
        organ.setDelFlag(DEL_FALSE);
        this.baseDaoImpl.save(organ);
        return "success";
    }
    
    @Override
    public String updateOrgan(Organ organ, String userName) {
        //验证机构编码的唯一性
        List<Organ> validateOrganList;
        validateOrganList = this.organDao.checkOrgan(organ, userName);
        if (!validateOrganList.isEmpty()) {
            return "organCodeExist";
        }
        List<Organ> childOrganList;
        childOrganList = this.getChildsOrgan(organ.getChildren());
        for (Organ childOrgan : childOrganList) {
            if (organ.getParentOrgan() != null && childOrgan.getId().equals(organ.getParentOrgan().getId())) {
                return "parentOrganNotChildOrgan";
            }
        }
        organ.setUpdateUser(userName);
        organ.setUpdateDate(new Date());
        this.baseDaoImpl.getHibernateTemplate().merge(organ);
        //this.baseDaoImpl.update(organ);// 会造成异常
        return "success";
    }

    @Override
    public String treeDropMove(String sourceNode, String targetNode, String username, String dropPosition) {
        Organ organ;
        organ = this.baseDaoImpl.findUniqueByProperty(Organ.class, "id", sourceNode);
        if ("append".equals(dropPosition)) {
            //在树显示的是在父节点中插入子节点
            organ.setParentOrgan(this.baseDaoImpl.findUniqueByProperty(Organ.class, "id", targetNode));
        } else {
            //在targetNode节点旁边插入兄弟节点
            if (this.baseDaoImpl.findUniqueByProperty(Organ.class, "id", targetNode).getParentOrgan() != null) {
                //拖拽子节点不在根目录时
                organ.setParentOrgan(this.baseDaoImpl.findUniqueByProperty(Organ.class, "id",
                      this.baseDaoImpl.findUniqueByProperty(Organ.class, "id", targetNode).getParentOrgan().getId()));
            } else {
                organ.setParentOrgan(null);
            }
        }
        organ.setUpdateDate(new Date());
        organ.setUpdateUser(username);
        this.baseDaoImpl.update(organ);
        return "success";
    }
    
    @Override
    public String treeDropCopy(String sourceNode, String targetNode, String username, String dropPosition) {
        Organ organSource;
        //获得要复制的节点
        organSource = this.baseDaoImpl.findUniqueByProperty(Organ.class, "id", sourceNode);
        Organ organNew;
        organNew = new Organ();
        organNew.setOrganName(organSource.getOrganName());
        organNew.setOrganCode(System.currentTimeMillis() + "");
        organNew.setOrganOrder(organSource.getOrganOrder());
        organNew.setDutyUsername(organSource.getDutyUsername());
        organNew.setRemark(organSource.getRemark());
        //organNew.setUserList(organSource.getUserList());
        organNew.setDelFlag(DEL_FALSE);
        organNew.setCreateDate(new Date());
        organNew.setCreateUser(username);
        
        if ("append".equals(dropPosition)) {
            //在树显示的是在父节点中插入子节点
            organNew.setParentOrgan(this.baseDaoImpl.findUniqueByProperty(Organ.class, "id", targetNode));
        } else {
            //在targetNode节点旁边插入兄弟节点
            if (this.baseDaoImpl.findUniqueByProperty(Organ.class, "id", targetNode).getParentOrgan() != null) {
                //拖拽子节点不在根目录时
                organNew.setParentOrgan(this.baseDaoImpl.findUniqueByProperty(Organ.class, "id",
                      this.baseDaoImpl.findUniqueByProperty(Organ.class, "id", targetNode).getParentOrgan().getId()));
            } else {
                organNew.setParentOrgan(null);
            }
        }
        this.baseDaoImpl.save(organNew);
        return "success";
    }


}
