package com.swsm.system.dao.impl;

import com.core.dao.IBaseDao;
import com.core.dao.impl.BaseDaoImpl;
import com.swsm.system.model.CodeDefDetail;
import org.springframework.stereotype.Repository;


/**
 * <p>ClassName: CodeDefDetailDaoImpl</p>
 * <p>Description: 编码明细表数据访问层接口实现</p>
 */
@Repository("codeDefDetailDao")
public class CodeDefDetailDaoImpl extends BaseDaoImpl<CodeDefDetail, String> implements IBaseDao<CodeDefDetail, String> {

}
