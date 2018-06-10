package com.swsm.system.service.impl;

import com.core.dao.impl.BaseDaoImpl;
import com.core.service.impl.EntityServiceImpl;
import com.core.tools.Filter;
import com.swsm.system.dao.impl.TaskConfigDaoImpl;
import com.swsm.system.model.TaskConfig;
import com.swsm.system.service.ITaskConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>ClassName: TaskConfigServiceImpl</p>
 * <p>Description: 任务调度配置的业务处理类</p>
 */
@Service("taskConfigService")
public class TaskConfigServiceImpl extends EntityServiceImpl<TaskConfig> implements ITaskConfigService {

    @Autowired
    @Qualifier("taskConfigDao")
    @Override
    public void setBaseDao(BaseDaoImpl<TaskConfig, String> baseDaoImpl) {
        this.baseDaoImpl = baseDaoImpl;
    }
    /**
     * 实例化taskConfigDao
     * 
     */
    @Autowired
    @Qualifier("taskConfigDao")
    private TaskConfigDaoImpl taskConfigDao;

    @Override
    public List<Map<String, Object>> getTaskConfig(Filter filter) {
        List<String> params;
        params = new ArrayList<>();
        if (checkValid("taskName", filter.getQueryMap())) {
            params.add("taskName");
        }
        if (checkValid("taskCode", filter.getQueryMap())) {
            params.add("taskCode");
        }
        List<Map<String, Object>> list;
        list = this.taskConfigDao.getTaskConfig(filter, params);
        return list;
    }

    @Override
    public String useTaskConfig(String taskCode) {
        this.taskConfigDao.useTaskConfig(taskCode);
        return "success";
    }

    @Override
    public String forbidTaskConfig(String taskCode) {
        this.taskConfigDao.forbidTaskConfig(taskCode);
        return "success";
    }

    @Override
    public String updateCronExpression(String taskCode, String cronExpression) {
        this.taskConfigDao.updateCronExpression(taskCode, cronExpression);
        return "success";
    }

    @Override
    public void saveTaskConfig(List<TaskConfig> taskConfigList) {
        for (TaskConfig t : taskConfigList) {
            t.setDelFlag(DEL_FALSE);
            this.baseDaoImpl.update(taskConfigList);
        }
    }

    
}
