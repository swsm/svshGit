package com.swsm.log.model;

import com.core.entity.BaseModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * <p>
 * ClassName: OperateLog
 * </p>
 * <p>
 * Description: 操作日志表的Model类
 * </p>
 */
@Data
@Entity
@Table(name = "TT_OPERATE_LOG")
public class OperateLog extends BaseModel implements java.io.Serializable {
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

}
