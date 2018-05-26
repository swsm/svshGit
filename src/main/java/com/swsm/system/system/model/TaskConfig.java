package com.swsm.system.system.model;

import com.core.entity.BaseModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * <p>ClassName: TaskConfig</p>
 * <p>Description: 任务调度配置Model</p>
 */
@Data
@Entity
@Table(name = "SYS_TASK_CONFIG")
public class TaskConfig extends BaseModel implements java.io.Serializable{

    /**
     * <p>Field serialVersionUID: 序列号</p>
     */
    private static final long serialVersionUID = 1L;
    /**
     * 任务编码
     */
    @Column(name = "TASK_CODE")
    private String taskCode;
    /**
     * 任务名称
     */
    @Column(name = "TASK_NAME")
    private String taskName;

    /**
     * 是否启用
     */
    @Column(name = "ENABLE_FLAG")
    private String enableFlag;
   
    /**
     * 调度周期表达式
     */
    @Column(name = "CRON_EXPRESSION")
    private String cronExpression;


    
}
