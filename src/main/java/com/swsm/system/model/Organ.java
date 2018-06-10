package com.swsm.system.model;

import com.core.entity.BaseModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


/**
 * <p>ClassName: Organ</p>
 * <p>Description: 部门Model</p>
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "SysOrganCache")
@Table(name = "SYS_ORGAN")
public class Organ extends BaseModel {

    /**
     * <p>Field serialVersionUID: 序列号</p>
     */
    private static final long serialVersionUID = -1424626242076523087L;

    /**
     * 机构名
     */
    @Column(name = "organ_name")
    private String organName;

    /**
     * 机构代码
     */
    @Column(name = "organ_code") 
    private String organCode;
    
    /**
     * 负责人名称
     */
    @Column(name = "duty_username") 
    private String dutyUsername;

    /**
     * LDAP server中的Id
     */
    @Column(name = "ldap_organ_id")
    private String ldapOrganId;
    
    /**
     * 机构排序
     */
    @Column(name = "organ_order")
    private Integer organOrder;

    /**
     * 上级机构
     */
    @ManyToOne(fetch = FetchType.LAZY)  
    @JoinColumn(name = "PARENT_ID", referencedColumnName = "PK_ID")  
    private Organ parentOrgan; 
    /**
     * 下级机构列表
     */
    @OneToMany(mappedBy = "parentOrgan", cascade = {CascadeType.REMOVE }, fetch = FetchType.LAZY)
    private Set<Organ> children;

    /**
     * 机构下的人员列表，但是删除机构，不删除人员
     */
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "SYS_USER_ORGAN",
    joinColumns = {@JoinColumn(name = "ORGAN_ID", referencedColumnName = "PK_ID") },
    inverseJoinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "PK_ID") })
    private Set<User> userList = new HashSet<User>(); 

    /**
     * 默认构造函数
     */
    public Organ() { 
    }

    /**
     * <p>Description: 构造函数</p>
     * @param id 机构id
     */
    public Organ(String id) {
        super.setId(id);
    }
    
    /**
     * <p>Description: 构造函数</p>
     * @param id 机构id
     * @param organCode 机构Code
     */
    public Organ(String id, String organCode) {
        super.setId(id);
        this.setOrganCode(organCode);
    }
    
    /**
     * <p>Description: 判断是否是叶子节点</p>
     * @return boolean 是 true 否 false
     */
    public boolean getLeaf() {
        boolean leaf = true;
        if (this.children != null && !this.children.isEmpty()) {
            for (Organ o : this.children) {
                if ("0".equals(o.getDelFlag())) {
                    leaf = false;
                    break;
                }
            }
        }
        return leaf;
    }


    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public String getOrganCode() {
        return organCode;
    }

    public void setOrganCode(String organCode) {
        this.organCode = organCode;
    }

    public String getDutyUsername() {
        return dutyUsername;
    }

    public void setDutyUsername(String dutyUsername) {
        this.dutyUsername = dutyUsername;
    }

    public String getLdapOrganId() {
        return ldapOrganId;
    }

    public void setLdapOrganId(String ldapOrganId) {
        this.ldapOrganId = ldapOrganId;
    }

    public Integer getOrganOrder() {
        return organOrder;
    }

    public void setOrganOrder(Integer organOrder) {
        this.organOrder = organOrder;
    }

    public Organ getParentOrgan() {
        return parentOrgan;
    }

    public void setParentOrgan(Organ parentOrgan) {
        this.parentOrgan = parentOrgan;
    }

    public Set<Organ> getChildren() {
        return children;
    }

    public void setChildren(Set<Organ> children) {
        this.children = children;
    }

    public Set<User> getUserList() {
        return userList;
    }

    public void setUserList(Set<User> userList) {
        this.userList = userList;
    }
}
