package com.swsm.system.service.impl;

import com.core.dao.impl.BaseDaoImpl;
import com.core.exception.BaseException;
import com.core.service.impl.EntityServiceImpl;
import com.swsm.constant.ActionConstants;
import com.swsm.system.dao.impl.VarConfigDaoImpl;
import com.swsm.system.model.VarConfig;
import com.swsm.system.service.IVarConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * <p>ClassName: VarConfigServiceImpl</p>
 * <p>Description: 配置项管理的业务实现类</p>
 */
@Service("varConfigService")
public class VarConfigServiceImpl extends EntityServiceImpl<VarConfig> implements IVarConfigService {

    @Autowired
    @Qualifier("varConfigDao")
    @Override
    public void setBaseDao(BaseDaoImpl<VarConfig, String> baseDaoImpl) {
        this.baseDaoImpl = baseDaoImpl;
        
    }
    /**
     * 实例化varConfigDao
     * 
     */
    @Autowired
    @Qualifier("varConfigDao")
    private VarConfigDaoImpl varConfigDao;
    
    @Override
    protected Map<String, String> getOrderMap() {
        Map<String, String> orderMap;
        orderMap = new HashMap<>();
        orderMap.put("parentVarConfigName", "parentVarConfig.varName");
        return orderMap;
    }

    
    @Override
    public List<VarConfig> findVarConfigByPanrentId(String parentId) throws BaseException {
        if (ActionConstants.ROOT_NODE_ID.equals(parentId)) {
            List<VarConfig> list;
            list = this.varConfigDao.findVarConfigByPanrentId();
            return list;
        } else {
            List<VarConfig> list;
            list = this.varConfigDao.findVarConfigByPanrentId(parentId);
            return list;
        }
    }
    
    /**
     * <p>Description: 获取所有子 部门</p>
     * @param childesVarConfig 子部门
     * @return 子部门列表
     */
    public List<VarConfig> getChildesVarConfig(Set<VarConfig> childesVarConfig) {
        List<VarConfig> list;
        list = new ArrayList<VarConfig>();
        list.addAll(childesVarConfig);
        for (VarConfig var : childesVarConfig) {
            list.addAll(this.getChildesVarConfig(var.getChildren()));
        }
        return list;
    }

    @Override
    public String deleteVarConfig(String id, String userName) throws BaseException {
      //1.子部门删除标记设为 true
        List<VarConfig> childVarConfigList;
        childVarConfigList = this.getChildesVarConfig(
                this.baseDaoImpl.findByProperty(VarConfig.class, "id", id).get(0).getChildren());
        for (VarConfig var : childVarConfigList) {
            var.setUpdateDate(new Date());
            var.setUpdateUser(userName);
            var.setDelFlag(DEL_TRUE);
        }
        
        //2.父部门删除标记设为 true
        VarConfig varConfig;
        varConfig = this.findEntity(VarConfig.class, id);
        varConfig.setUpdateDate(new Date());
        varConfig.setUpdateUser(userName);
        varConfig.setDelFlag(DEL_TRUE);
        
        return "success";
    }

    @Override
    public String saveVarConfig(VarConfig varConfig, String userName) {
        //验证机构编码的唯一性
        List<VarConfig> varConfigName;
        List<VarConfig> varConfigList;
        varConfigName = this.varConfigDao.checkVarName(varConfig);
        varConfigList = this.varConfigDao.checkVarDisplay(varConfig);
        if(!varConfigName.isEmpty()){
            return "varNameExist";
        }
        if (!varConfigList.isEmpty()) {
            return "varDisplayExist";
        }
        varConfig.setCreateUser(userName);
        varConfig.setCreateDate(new Date());
        varConfig.setDelFlag(DEL_FALSE);
        this.baseDaoImpl.save(varConfig);
        return "success";
    }

    
    @Override
    public String updateVarConfig(VarConfig varConfig, String userName) {
      //验证机构编码的唯一性
        List<VarConfig> validateVarNameList;
        List<VarConfig> validateVarConfigList;
        validateVarNameList = this.varConfigDao.checkVarName(varConfig);
        validateVarConfigList = this.varConfigDao.checkVarDisplay(varConfig);
        if(!validateVarNameList.isEmpty() && !validateVarNameList.get(0).getVarName().equals(varConfig.getVarName())) {
            return "varNameExist";
        }
        if (!validateVarConfigList.isEmpty() && !validateVarConfigList.get(0).getVarDisplay().equals(varConfig.getVarDisplay())) {
            return "varDisplayExist";
        }
        List<VarConfig> childVarConfigList;
        childVarConfigList = this.getChildesVarConfig(varConfig.getChildren());
        for (VarConfig childVarConfig : childVarConfigList) {
            if (varConfig.getParentVarConfig() != null && childVarConfig.getId().equals(varConfig.getParentVarConfig().getId())) {
                return "parentVarConfigNotChildVarConfig";
            }
        }
        varConfig.setUpdateUser(userName);
        varConfig.setUpdateDate(new Date());
        this.baseDaoImpl.getHibernateTemplate().merge(varConfig);
        //this.baseDaoImpl.update(varConfig);// 会造成异常
        return "success";
    }

    @Override
    public String treeDropMove(String sourceNode, String targetNode, String userName, String dropPosition) {
        VarConfig varConfig;
        varConfig = this.baseDaoImpl.findUniqueByProperty(VarConfig.class, "id", sourceNode);
        if ("append".equals(dropPosition)) {
            //在树显示的是在父节点中插入子节点
            varConfig.setParentVarConfig(this.baseDaoImpl.findUniqueByProperty(VarConfig.class, "id", targetNode));
        } else {
            //在targetNode节点旁边插入兄弟节点
            if (this.baseDaoImpl.findUniqueByProperty(VarConfig.class, "id", targetNode).getParentVarConfig() != null) {
                //拖拽子节点不在根目录时
                varConfig.setParentVarConfig(this.baseDaoImpl.findUniqueByProperty(VarConfig.class, "id",
                      this.baseDaoImpl.findUniqueByProperty(VarConfig.class, "id", targetNode).getParentVarConfig().getId()));
            } else {
                varConfig.setParentVarConfig(null);
            }
        }
        varConfig.setUpdateDate(new Date());
        varConfig.setUpdateUser(userName);
        this.baseDaoImpl.update(varConfig);
        return "success";
    }

    @Override
    public String treeDropCopy(String sourceNode, String targetNode, String userName, String dropPosition) {
        VarConfig varConfigSource;
        //获得要复制的节点
        varConfigSource = this.baseDaoImpl.findUniqueByProperty(VarConfig.class, "id", sourceNode);
        VarConfig varConfigNew;
        varConfigNew = new VarConfig();
        varConfigNew.setVarName(varConfigSource.getVarName());
        varConfigNew.setVarDisplay(System.currentTimeMillis() + "");
        varConfigNew.setVarOrder(varConfigSource.getVarOrder());
        varConfigNew.setVarValue(varConfigSource.getVarValue());
        varConfigNew.setRemark(varConfigSource.getRemark());
        //varConfigNew.setUserList(varConfigSource.getUserList());
        varConfigNew.setDelFlag(DEL_FALSE);
        varConfigNew.setCreateDate(new Date());
        varConfigNew.setCreateUser(userName);
        
        if ("append".equals(dropPosition)) {
            //在树显示的是在父节点中插入子节点
            varConfigNew.setParentVarConfig(this.baseDaoImpl.findUniqueByProperty(VarConfig.class, "id", targetNode));
        } else {
            //在targetNode节点旁边插入兄弟节点
            if (this.baseDaoImpl.findUniqueByProperty(VarConfig.class, "id", targetNode).getParentVarConfig() != null) {
                //拖拽子节点不在根目录时
                varConfigNew.setParentVarConfig(this.baseDaoImpl.findUniqueByProperty(VarConfig.class, "id",
                      this.baseDaoImpl.findUniqueByProperty(VarConfig.class, "id", targetNode).getParentVarConfig().getId()));
            } else {
                varConfigNew.setParentVarConfig(null);
            }
        }
        this.baseDaoImpl.save(varConfigNew);
        return "success";
    }
    

}
