package com.swsm.log.model;

import lombok.Data;

/**
 * <p>ClassName: OperateContent</p>
 * <p>Description: 操作日志内容Model类</p>
 */
@Data
public class OperateContent {

    /**
     * 操作日志类型
     */
    private String operateType;
    
    /**
     * 操作内容名称
     */
    private String operateName;

}
