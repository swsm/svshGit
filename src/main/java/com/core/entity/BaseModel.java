package com.core.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

/**
 * 所有数据库model的基类，包含了
 * PK_ID,CREATE_USER,CREATE_DATE,UPDATE_USER,UPDATE_DATE,REMARK,DEL_FLAG等通用字段
 */
@Data
@MappedSuperclass
public class BaseModel {

    /**
     * 数据模型的唯一标识
     */
    @Id
    @Column(name = "PK_ID", columnDefinition = "char(32)")
    @GeneratedValue(generator = "huuid")     
    @GenericGenerator(name = "huuid", strategy = "uuid")
    private String id;
    
    /**
     * 备注
     */
    @Column(name = "REMARK", length = 4000)
    private String remark = null;
    /**
     * 删除标记
     */
    @Column(name = "DEL_FLAG")
    private String delFlag = null;
    /**
     * 创建人
     */
    @Column(name = "CREATE_USER", updatable = false)
    private String createUser = null;
    
    /**
     * 记录创建时间
     */
    @Column(name = "CREATE_DATE", updatable = false)
    private Date createDate = null;
    
    /**
     * 更新人
     */
    @Column(name = "UPDATE_USER")
    private String updateUser = null;
    
    /**
     * 记录更新时间
     */
    @Column(name = "UPDATE_DATE")
    private Date updateDate = null;

}