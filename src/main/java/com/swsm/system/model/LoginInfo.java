package com.swsm.system.model;

import com.core.entity.BaseModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;


/**
 * <p>ClassName: LoginInfo</p>
 * <p>Description: LoginInfo的model</p>
 */
@Data
@Entity
@Table(name = "SYS_LOGIN_INFO")
public class LoginInfo extends BaseModel implements java.io.Serializable {

    /**
     * 序列号
     */
    private static final long serialVersionUID = 7530202458836884341L;
    
    /**
     * 用户账号
     */
    @Column(name = "USER_NAME") 
    private String userName;
    
    /**
     * 用户姓名
     */
    @Column(name = "TRUE_NAME") 
    private String trueName;
    
    /**
     * 用户ip
     */
    @Column(name = "IP_ADDRESS")
    private String ipAddress;
    
    /**
     * 最近访问模块
     */
    @Column(name = "MODULE_NAME") 
    private String moduleName;
    
    /**
     * 登录时间
     */
    @Column(name = "LOGIN_TIME") 
    private Date loginTime;
    
    /**
     * 登录状态
     */
    @Column(name = "LOGIN_STATUS")
    private String loginStatus;
    
    /**
     * 注销原因
     */
    @Column(name = "LOGOFF_TAG")
    private String logoffTag;

}
