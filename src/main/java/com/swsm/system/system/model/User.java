package com.swsm.system.system.model;

import com.core.entity.BaseModel;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * <p>ClassName: User</p>
 * <p>Description: 用户Model</p>
 */
@Entity
@Table(name = "sys_user")
public class User extends BaseModel implements java.io.Serializable {

    /**
     * 序列号
     */
    private static final long serialVersionUID = 7530202458836884341L;

    // Fields
    /**
     * 用户账号
     */
    @Column(name = "username") 
    private String username;

    /**
     * 用户密码
     */
    @Column(name = "password") 
    private String password;

    /**
     * 用户姓名
     */
    @Column(name = "truename") 
    private String truename;

    /**
     * 用户座机
     */
    @Column(name = "telephone") 
    private String telephone;

    /**
     * 用户电子邮件
     */
    @Column(name = "email") 
    private String email;

    /**
     * 手机号码
     */
    @Column(name = "mobile") 
    private String mobile;
    
    /**
     * 用户住址
     */
    @Column(name = "address") 
    private String address;


    /**
     * 工号
     */
    @Column(name = "WORK_NO") 
    private String workNo;

    /**
     * LDAP server中的Id
     */
    @Column(name = "ldap_user_id") 
    private String ldapUserId;
    
    /**
     * 开启状态
     */
    @Column(name = "enabled")
    private String enabled;

    /**
     * 角色名称字符串
     */
    @Column(name = "roleNameStr")
    private String roleNameStr;
    
    /**
     * 微信号
     */
    @Column(name = "WECHAT_NAME")
    private String wechatName;

    /**
     * 所拥有的角色列表
     */
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "SYS_USER_ROLE", 
    joinColumns = { @JoinColumn(name = "USER_ID", referencedColumnName = "PK_ID") }, 
    inverseJoinColumns = { @JoinColumn(name = "ROLE_ID", referencedColumnName = "PK_ID") }) 
    private  Set<Role> roleList = new HashSet<Role>();

    /**
     * 机构名
     */
    @Transient
    private String organName = null;
    /**
     * 机构id
     */
    @Transient
    private String organId = null;
    /**
     * 机构code
     */
    @Transient
    private String organCode = null;

    /**
     * 对应的部门（当前是一个用户一个部门），但是也通过中间表绑定关系
     */
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "SYS_USER_ORGAN",
    joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "PK_ID") },
    inverseJoinColumns = {@JoinColumn(name = "ORGAN_ID", referencedColumnName = "PK_ID") })
    private  Set<Organ> organList = new HashSet<Organ>(); 


    /**
     * <p>Description: 角色名</p>
     * @return RoleId数组
     */
    public String[] getRoleNames() {
        String[] roleNames;
        roleNames = StringUtils.split(this.getRoleNameStr(), ", ");
        return roleNames;
    }
    
    /**
     * <p>Description: 取得RoleId数组</p>
     * @return RoleId数组
     */
    public String[] getRoleId() {
        List<String> roleStringList;
        roleStringList = new ArrayList<String>();
        for (Role role: this.roleList) {
            if (StringUtils.equals("1", role.getEnabled())) {
                roleStringList.add(role.getId());
            }
        }
        String[] a;
        a = new String[]{};
        return roleStringList.toArray(a);
    }
    
    /**
     * <p>Description: 所在机构id</p>
     * @return 机构id
     */
    public String getOrganId() {
        if (StringUtils.isNotEmpty(this.organId)) {
            return this.organId;
        }
        if (!this.organList.isEmpty()) {
            Organ[] arr;
            arr = new Organ[]{};
            return (this.organList.toArray(arr))[0].getId();
        }
        return null;
    }
    
    /**
     * <p>Description: 所在机构名称</p>
     * @return 机构名称
     */
    public String getOrganName() {
        if (StringUtils.isNotEmpty(this.organName)) {
            return this.organName;
        }
        
        if (!this.organList.isEmpty()) {
            Organ[] arr;
            arr = new Organ[]{};
            return (this.organList.toArray(arr))[0].getOrganName();
        }
        return null;
    }
    
    /**
     * <p>Description: 所在机构代码</p>
     * @return 机构代码
     */
    public String getOrganCode() {
        if (StringUtils.isNotEmpty(this.organCode)) {
            return this.organCode;
        }
        if (!this.organList.isEmpty()) {
            Organ[] arr;
            arr = new Organ[]{};
            return (this.organList.toArray(arr))[0].getOrganName();
        }
        return null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWorkNo() {
        return workNo;
    }

    public void setWorkNo(String workNo) {
        this.workNo = workNo;
    }

    public String getLdapUserId() {
        return ldapUserId;
    }

    public void setLdapUserId(String ldapUserId) {
        this.ldapUserId = ldapUserId;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getRoleNameStr() {
        return roleNameStr;
    }

    public void setRoleNameStr(String roleNameStr) {
        this.roleNameStr = roleNameStr;
    }

    public String getWechatName() {
        return wechatName;
    }

    public void setWechatName(String wechatName) {
        this.wechatName = wechatName;
    }

    public Set<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(Set<Role> roleList) {
        this.roleList = roleList;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public void setOrganId(String organId) {
        this.organId = organId;
    }

    public void setOrganCode(String organCode) {
        this.organCode = organCode;
    }

    public Set<Organ> getOrganList() {
        return organList;
    }

    public void setOrganList(Set<Organ> organList) {
        this.organList = organList;
    }
}
