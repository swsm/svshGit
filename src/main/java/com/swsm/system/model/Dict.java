package com.swsm.system.model;

import com.core.entity.BaseModel;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * <p>ClassName: Dict</p>
 * <p>Description: 字典明细的Model类</p>
 */
@Data
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "SysDictCache")
@Table(name = "SYS_DICT")
public class Dict extends BaseModel implements java.io.Serializable {

    /**
     * 序列号
     */
    private static final long serialVersionUID = 7530202458836884341L;

    // Fields
    /**
     * 字典键值
     */
    @Column(name = "dict_key")
    private String dictKey;

    /**
     * 字典显示值
     */
    @Column(name = "dict_value")
    private String dictValue;

    /**
     * 排序
     */
    @Column(name = "dict_sort")
    private int dictSort;

    /**
     * 是否开启
     */
    @Column(name = "open_flag")
    private String openFlag;
   
    /**
     * 字典类型
     */
    @Column(name = "parent_key")
    private String parentKey;

    /**
     * 默认的构造函数
     */
    public Dict() {
    }
    
    /**
     * <p>
     * Description: 构造函数
     * </p>
     * 
     * @param dictKey 字典Key
     * @param dictValue 字典值
     */
    public Dict(String dictKey, String dictValue) {
        this.setDictKey(dictKey);
        this.setDictValue(dictValue);
    }


    /**
     * 
     * <p>
     * Description: 是否相等
     * </p>
     * 
     * @param obj 比较对象
     * @return true or false
     */
    public boolean equals(Object obj) {
        if (StringUtils.equals(this.dictKey, ((Dict) obj).getDictKey())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 重写hashCode
     * @return int
     */
     public int hashCode() {
         final int  CODE = 7;
         return this.dictKey.hashCode() << CODE;
     }
}