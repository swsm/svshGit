package com.swsm.system.system.model;

import com.core.entity.BaseModel;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>ClassName: Role</p>
 * <p>Description: 角色Model</p>
 */
@Entity
@Table(name = "SYS_ROLE")
public class Role extends BaseModel implements java.io.Serializable {

    /**
     * 序列号
     */
    private static final long serialVersionUID = 7530202458836884341L;

    // Fields
    /**
     * 角色名
     */
    @Column(name = "ROLE_NAME")
    private String roleName;

    /**
     * 角色类别
     */
    @Column(name = "ROLE_TYPE")
    private String roleType;
    
    /**
     * 是否可以忽略决策
     */
    @Column(name = "IGNORE_DECESION")
    private String ignoreDecesion;

    /**
     * 状态
     */
    @Column(name = "ENABLED")
    private String enabled;

    /**
     * 被哪些用户引用
     */
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "SYS_USER_ROLE", joinColumns = { @JoinColumn(name = "ROLE_ID", referencedColumnName = "PK_ID") }, 
        inverseJoinColumns = { @JoinColumn(name = "USER_ID", referencedColumnName = "PK_ID") })
    private  Set<User> userList = new HashSet<User>();

    /**
     * 对应哪些资源
     */
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "SYS_ROLE_RESOURCE", joinColumns = { @JoinColumn(name = "ROLE_ID", referencedColumnName = "PK_ID")
        }, inverseJoinColumns = { @JoinColumn(name = "RES_ID", referencedColumnName = "PK_ID") })
    private  Set<Resource> resList = new HashSet<Resource>();

    // Constructors

    /**
     *  默认构造函数
     */
    public Role() {
    }

    /**
     * <p>
     * Description: 根据角色id构造一个对象
     * </p>
     * 
     * @param id 角色id
     */
    public Role(String id) {
        super.setId(id);
    }


    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getIgnoreDecesion() {
        return ignoreDecesion;
    }

    public void setIgnoreDecesion(String ignoreDecesion) {
        this.ignoreDecesion = ignoreDecesion;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public Set<User> getUserList() {
        return userList;
    }

    public void setUserList(Set<User> userList) {
        this.userList = userList;
    }

    public Set<Resource> getResList() {
        return resList;
    }

    public void setResList(Set<Resource> resList) {
        this.resList = resList;
    }
}