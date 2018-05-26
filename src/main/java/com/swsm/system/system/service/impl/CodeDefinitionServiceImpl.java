package com.swsm.system.system.service.impl;

import com.core.dao.impl.BaseDaoImpl;
import com.core.service.impl.EntityServiceImpl;
import com.core.tools.Filter;
import com.core.tools.MesBaseUtil;
import com.swsm.system.system.dao.impl.CodeDefDetailDaoImpl;
import com.swsm.system.system.dao.impl.CodeDefinitionDaoImpl;
import com.swsm.system.system.model.CodeDefDetail;
import com.swsm.system.system.model.CodeDefinition;
import com.swsm.system.system.service.ICodeDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>ClassName: CodeDefinitionServiceImpl</p>
 * <p>Description: 编码定义接口实现</p>
 */
@Service("codeDefinitionService")
public class CodeDefinitionServiceImpl extends EntityServiceImpl<CodeDefinition> implements ICodeDefinitionService {

    /**
     * ICodeDefinitionDao
     */
    @Autowired
    @Qualifier("codeDefinitionDao")
    private CodeDefinitionDaoImpl codeDefinitionDao;
    
    /**
     * ICodeDefDetailDao
     */
    @Autowired
    @Qualifier("codeDefDetailDao")
    private CodeDefDetailDaoImpl codeDefDetailDao;
    

    @Autowired
    @Qualifier("codeDefinitionDao")
    @Override
    public void setBaseDao(BaseDaoImpl<CodeDefinition, String> baseDaoImpl) {
        this.baseDaoImpl = baseDaoImpl;
    }
    
    @Override
    public void saveCodeDefinition(List<CodeDefinition> codeList, String[] userInfo) {
        for (CodeDefinition code : codeList) {
            if (code.getId() == null) {
                MesBaseUtil.setDefaultFiledsBaseModel(code, userInfo, true);
                this.codeDefinitionDao.save(code);
                this.addCodeDefDetail(code, userInfo);
            } else {
                int segmentNum;
                segmentNum = this.codeDefinitionDao.getSegmentNumById(code.getId());
                MesBaseUtil.setDefaultFiledsBaseModel(code, userInfo, false);
                this.codeDefinitionDao.update(code);
                if (segmentNum != code.getSegmentNum()) {
                    this.deleteCodeDefDetail(code.getId());
                    this.addCodeDefDetail(code, userInfo);
                }
            }
        }
    }

    /**
     * 
     * <p>Description: 新增空的编码明细记录</p>
     * @param code 编码定义
     * @param userInfo 用户信息
     */
    private void addCodeDefDetail(CodeDefinition code, String[] userInfo) {
        List<CodeDefDetail> detList;
        detList = new LinkedList<>();
        for (int i = 0; i < code.getSegmentNum(); i++) {
            CodeDefDetail det;
            det = new CodeDefDetail();
            det.setCodeDefId(code.getId());
            det.setSerialNum(i + 1);
            MesBaseUtil.setDefaultFiledsBaseModel(det, userInfo, true);
            detList.add(det);
        }
        this.codeDefDetailDao.save(detList);
    }

    @Override
    public void deleteCodeDefinition(String[] ids) {
        for (String id : ids) {
            this.deleteCodeDefDetail(id);
            this.codeDefinitionDao.deleteById(CodeDefinition.class, id);
        }
    }
    
    /**
     * 
     * <p>Description: 根据编码id删除明细</p>
     * @param codeDefId 编码id
     */
    private void deleteCodeDefDetail(String codeDefId){
        List<CodeDefDetail> detList;
        detList = this.codeDefDetailDao.findByProperty(CodeDefDetail.class, "codeDefId", codeDefId);
        this.codeDefDetailDao.delete(detList);
    }

    @Override
    public List<CodeDefinition> queryCodeDefinition(Filter filter) {
        return this.codeDefinitionDao.queryCodeDefinition(filter);
    }

    @Override
    public List<CodeDefDetail> getCodeDefDetail(String codeId) {
        return this.codeDefDetailDao.findByProperty(CodeDefDetail.class, "codeDefId", codeId);
    }

    @Override
    public void saveCodeDefDetail(List<CodeDefDetail> detList, String[] userInfo) {
        for (CodeDefDetail det : detList) {
            MesBaseUtil.setDefaultFiledsBaseModel(det, userInfo, false);
        }
        this.codeDefDetailDao.update(detList);
    }

}
