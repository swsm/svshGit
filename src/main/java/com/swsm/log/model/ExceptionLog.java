package com.swsm.log.model;

import com.core.entity.BaseModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * <p>ClassName: ExceptionLog</p>
 * <p>Description: 异常日志表的MODEL类</p>
 */
@Data
@Entity
@Table(name = "TT_EXCEPTION_LOG")
public class ExceptionLog extends BaseModel implements java.io.Serializable {

    /**
     * 序列号
     */
    private static final long serialVersionUID = 1L;

    /******************* members ******************/
    
    /**
     * 模块名称
     */
    @Column(name = "MODUAL")
    private String modual;    
    
    /**
     * 用户姓名
     */
    @Column(name = "USERNAME")
    private String username;    
    
    /**
     * 地址IP
     */
    @Column(name = "REMOTE_IP")
    private String remoteIp;    
    
    /**
     * 日志内容
     */
    @Column(name = "LOG_CONTENT")
    private String logContent;    
    
    /**
     * 用户ID
     */
    @Column(name = "USER_ID")
    private String userId;
    /**
     * 异常类别
     */
    @Column(name = "EXCEPTION_TYPE")
    private String exceptionType;
    /**
     * 异常原因
     */
    @Column(name = "EXCEPTION_REASON")
    private String exceptionReason;
    /**
     * 消息标识
     */
    @Column(name = "MESSAGE_FLAG")
    private String messageFlag;
    /**
     * 数据项
     */
    @Column(name = "DATA_ITEMS")
    private String dataItems;
    

}
