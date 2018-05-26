package com.swsm.system.log.model;

import com.core.entity.BaseModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * <p>
 * ClassName: AccessLog
 * </p>
 * <p>
 * Description: 访问日志表的Model类
 * </p>
 */
@Data
@Entity
@Table(name = "TT_ACCESS_LOG")
public class AccessLog extends BaseModel implements java.io.Serializable {
    /**
     * 序列号
     */
    private static final long serialVersionUID = 1L;

    /******************* members ******************/

    /**
     * 访问用户姓名
     */
    @Column(name = "USERNAME")
    private String username;

    /**
     * 访问地址IP
     */
    @Column(name = "REMOTE_IP")
    private String remoteIp;

    /**
     * 访问模块
     */
    @Column(name = "MODUAL")
    private String modual;

    /**
     * 日志内容
     */
    @Column(name = "LOG_CONTEN")
    private String logConten;

    /**
     * 访问用户ID
     */
    @Column(name = "USER_ID")
    private String userId;



}
