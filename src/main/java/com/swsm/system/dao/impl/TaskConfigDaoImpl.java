package com.swsm.system.dao.impl;

import com.core.dao.impl.BaseDaoImpl;
import com.core.tools.Filter;
import com.swsm.system.model.TaskConfig;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * <p>ClassName: TaskConfigDao</p>
 * <p>Description: 任务调度配置的数据库操作类</p>
 */
@Repository("taskConfigDao")
public class TaskConfigDaoImpl extends BaseDaoImpl<TaskConfig, String> {

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getTaskConfig(Filter filter, List<String> params) {
        StringBuilder sql;
        sql = new StringBuilder();
        sql.append("select PK_ID as \"id\", TASK_CODE as \"taskCode\", ");
        sql.append(" TASK_NAME as \"taskName\", ENABLE_FLAG as \"enableFlag\",");
        sql.append(" CRON_EXPRESSION as \"cronExpression\",REMARK as \"remark\" from SYS_TASK_CONFIG ");
        sql.append(" where DEL_FLAG = '0'");
        if (params.contains("taskName")) {
            sql.append(" and TASK_NAME = :taskName");
        }
        if (params.contains("taskName")) {
            sql.append(" and TASK_CODE = :taskCode");
        }
        Object obj;
        obj = this.findBySqlQuery("select count(*) from (" + String.valueOf(sql) + " ) tt",
                filter.getQueryMap()).uniqueResult();
        if (obj != null) {
            filter.getPageInfo().count = Integer.parseInt(String.valueOf(obj));
        }
        return findBySqlQuery(sql.toString(), filter.getQueryMap()).setFirstResult(filter.getPageInfo().start)
                .setMaxResults(filter.getPageInfo().limit)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        
    }

    public void useTaskConfig(String taskCode) {
        String sql;
        sql = "update SYS_TASK_CONFIG set ENABLE_FLAG = '1' where TASK_CODE = '" + taskCode + "'";
        updateBySqlQuery(sql, null);
    }

    public void forbidTaskConfig(String taskCode) {
        String sql;
        sql = "update SYS_TASK_CONFIG set ENABLE_FLAG = '0' where TASK_CODE = '" + taskCode + "'";
        updateBySqlQuery(sql, null);
    }

    public void updateCronExpression(String taskCode, String cronExpression) {
        String sql;
        sql = " update SYS_TASK_CONFIG set CRON_EXPRESSION = '" + cronExpression + "' " 
            + " where TASK_CODE = '" + taskCode + "'";
        updateBySqlQuery(sql, null);
        
    }

}
