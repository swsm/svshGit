package com.swsm.system.model;

import com.core.entity.BaseModel;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


/**
 * <p>ClassName: VarConfig</p>
 * <p>Description: 配置项管理的Model</p>
 */
@Data
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "SysVarConfigCache")
@Table(name = "SYS_VAR_CONFIG")
public class VarConfig extends BaseModel implements Serializable{
    
    
    /**
     * <p>Field serialVersionUID: 序列号</p>
     */
    private static final long serialVersionUID = 1L;
    /**
     * 配置项名称
     */
    @Column(name = "VAR_NAME")
    private String varName;
    /**
     * 配置项编码
     */
    @Column(name = "VAR_DISPLAY")
    private String varDisplay;
    /**
     * 配置项值
     */
    @Column(name = "VAR_VALUE")
    private String varValue;
    /**
     * 类型
     */
    @Column(name = "VAR_TYPE")
    private String varType;
    /**
     * 排序
     */
    @Column(name = "VAR_ORDER")
    private Integer varOrder;
    /**
     * 所属层级id
     */
    @ManyToOne(fetch = FetchType.LAZY)  
    @JoinColumn(name = "PARENT_ID", referencedColumnName = "PK_ID")  
    private VarConfig parentVarConfig;
    
    /**
     * 子列表
     */
    @OneToMany(mappedBy = "parentVarConfig", cascade = {CascadeType.REMOVE }, fetch = FetchType.LAZY)
    private Set<VarConfig> children;
    
    /**
     * 机构下的人员列表，但是删除机构，不删除人员
     */
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "SYS_USER_VARCONFIG",
    joinColumns = {@JoinColumn(name = "VARCONFIG_ID", referencedColumnName = "PK_ID") },
    inverseJoinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "PK_ID") })
    private Set<User> userList = new HashSet<User>(); 
    
    
    /**
     * 默认构造函数
     */
    public VarConfig() { 
    }
    
    /**
     * <p>Description: 构造函数</p>
     * @param id 配置项id
     */
    public VarConfig(String id) {
        super.setId(id);
    }
    
    /**
     * <p>Description: 构造函数</p>
     * @param id 配置项id
     * @param varDisplay 配置项编码
     */
    public VarConfig(String id, String varDisplay) {
        super.setId(id);
        this.setVarDisplay(varDisplay);
    }
    
    /**
     * <p>Description: 判断是否是叶子节点</p>
     * @return boolean 是 true 否 false
     */
    public boolean getLeaf() {
        boolean leaf = false;
        if (!"1".equals(varType))
            return true;
        return leaf;
    }

}
