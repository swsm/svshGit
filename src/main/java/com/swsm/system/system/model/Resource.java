package com.swsm.system.system.model;

import com.core.entity.BaseModel;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>ClassName: Resource</p>
 * <p>Description: 资源Model</p>
 */
@Data
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONE,  region = "SysResourceCache")
@Table(name = "SYS_RESOURCE")
public class Resource extends BaseModel implements java.io.Serializable {

    /**
     * 序列号
     */ 
    private static final long serialVersionUID = 7530202458836884341L;

    // Fields
    /**
     * 资源名
     */
    @Column(name = "RES_NAME")
    private String resName;

    /**
     * 资源代码
     */
    @Column(name = "RES_CODE")
    private String resCode;

    /**
     * 资源类别
     */
    @Column(name = "RES_TYPE")
    private String resType;

    /**
     * 资源排序
     */
    @Column(name = "RES_ORDER") 
    private Integer resOrder;
    
    /**
     * 资源状态
     */
    @Column(name = "ENABLED")
    private String enabled;
    
    /**
     * 所属系统
     */
    @Column(name = "BELONG_SYSTEM")
    private String belongSystem;
    
    
    /**
     * 
     */
    @Column(name = "MODUAL_FLAG")
    private String modualFalg;

    /**
     * 是否与当前角色关联
     */
    @Transient
    private boolean checked = false;
    
    /**
     * 上级id
     */
    @Transient
    private int childCount ;
    /**
     * 子节点数量
     */
    @Transient
    private String parentId;
    
    /**
     * 父资源id str
     */
    @Transient
    private String parentIdStr;
    
    /**
     * 数据库中查出来的是否选中 1选中0未选中
     */
    @Transient
    private String dbChecked;
    
    /**
     * 被哪些角色引用
     */
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "SYS_ROLE_RESOURCE", 
    joinColumns = { @JoinColumn(name = "RES_ID", referencedColumnName = "PK_ID") }, 
    inverseJoinColumns = { @JoinColumn(name = "ROLE_ID", referencedColumnName = "PK_ID") }) 
    private Set<Role> roleList = new HashSet<Role>();  
    
    /**
     * 上级资源
     */
    @ManyToOne(fetch = FetchType.LAZY)  
    @JoinColumn(name = "PARENT_ID", referencedColumnName = "PK_ID")  
    private Resource parentResource; 
    
    /**
     * 下级资源列表
     */
    @OneToMany(mappedBy = "parentResource", cascade = {CascadeType.REMOVE }, fetch = FetchType.LAZY)
    private Set<Resource> childResList;
    
    /**
     * 下级资源列表
     */
    @Transient
    private Set<Resource> children;
    
    /**
     * 上级菜单名
     */
    @Transient
    private String preResName;
    
    /*
     * 菜单图标class
     */
    @Column(name = "icon_cls", length = 50)
    private String iconCls;
    
    //Constructors

    /**
     * 默认构造函数
     */
    public Resource() {
    }

    /**
     * <p>Description: 构造函数</p>
     * @param id 资源Id
     * @param resName 资源名
     */
    public Resource(String id, String resName) {
        this.setId(id);
        this.setResName(resName);
    }


    /**
     * <p>Description: 移除子节点</p>
     * @param resId 资源Id
     */
    public void removeChild(String resId) {
        for (Resource child : this.children) {
            if (StringUtils.equals(resId, child.getId())) {
                this.children.remove(child);
                break;
            }
            child.removeChild(resId);
        }
    }

}