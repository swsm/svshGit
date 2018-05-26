package com.swsm.system.system.service;

import com.core.tools.Filter;
import com.swsm.system.system.model.TaskConfig;

import java.util.List;
import java.util.Map;


/**
 * <p>ClassName: ITaskConfigService</p>
 * <p>Description: 任务调度配置接口</p>
 */
public interface ITaskConfigService {
    /**
     * 
     * <p>Description: 查询任务调度配置信息</p>
     * @param filter 查询条件组装的过滤器
     * @return 任务调度配置信息
     */
    List<Map<String, Object>> getTaskConfig(Filter filter);
    
    /**
     * 
     * <p>Description: 启用任务调度配置</p>
     * @param taskCode 任务编码
     * @return success字符串
     */
    String useTaskConfig (String taskCode); 
    
    /**
     * 
     * <p>Description: 禁用任务调度配置</p>
     * @param taskCode 任务编码
     * @return success字符串
     */
    String forbidTaskConfig (String taskCode); 
    
    /**
     * 
     * <p>Description: 更新任务调度配置</p>
     * @param taskCode 任务编码
     * @param cronExpression 调度设置信息
     * @return success字符串
     */
    String updateCronExpression(String taskCode,String cronExpression);

    /**
     * 
     * <p>Description: 更新任务调度配置中的任务名称和备注字段</p>
     * @param list 修改的字段信息
     */
    void saveTaskConfig(List<TaskConfig> list);
   
}
