package com.swsm.log.model;

import com.core.entity.BaseModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * <p>ClassName: InterfaceLog</p>
 * <p>Description: 接口日志的MODEL类</p>
 */
@Data
@Entity
@Table(name = "TT_INTERFACE_LOG")
public class InterfaceLog extends BaseModel implements java.io.Serializable{

    /**
     * <p>Field serialVersionUID: 序列化</p>
     */
    private static final long serialVersionUID = 1L;

    /**
     * 接口名称
     */
    @Column(name = "INTERFACE_NAME")
    private String interfaceName;
    /**
     * 接口地址ip
     */
    @Column(name = "INTERFACE_IP")
    private String interfaceIp;
    /**
     * 日志内容
     */
    @Column(name = "LOG_CONTENT")
    private String logContent;
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
