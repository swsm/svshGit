/**
 * CommonConstants.java
 * Created at 2015-11-24
 */
package com.swsm.constant;

/**
 * <p>ClassName: CommonConstants</p>
 * <p>Description: 通用常量类，整个系统通用的</p>
 */
public class CommonConstants {
    
    /**
     * 物料类型，原材料
     */
    public static final String MATERIAL_TYPE_RAW = "0";
    
    /**
     * 物料类型，Pack材料
     */
    public static final String MATERIAL_TYPE_RAWPACK = "1";
    
    /**
     * 物料类型，电芯型号（半成品）
     */
    public static final String MATERIAL_TYPE_CELL = "2";
    

    /**
     * 物料类型，Pack型号（成品）
     */
    public static final String MATERIAL_TYPE_PACK = "3";
    
    
    /**
     * 物料类型，WIP型号（在制品）
     */
    public static final String MATERIAL_TYPE_WIP = "5";
    
    
    /**
     * 物料类型，备件
     */
    public static final String MATERIAL_TYPE_SPARE = "1";

    /**
     * 不限
     */
    public static final String NOT_LIMIT = "不限";
    
    /**
     * 未删除标识
     */
    public static final String DEL_FALSE = "0";
    
    /**
     * 删除标识
     */
    public static final String DEL_TRUE = "1";
    
    /**
     * rows
     */
    public static final String ROWS = "rows";
    
    /**
     * total
     */
    public static final String TOTAL = "total";

    /**
     * 启用标记，启用，有效
     */
    public static final String OPEN_FLAG = "1";
    
    /**
     * 操作成功
     */
    public static final String SUCCESS = "success";
    
    /**
     * 密码规则，true符合
     */
    public static final String PASSWORDROLE = "true";
    
    /**
     * 密码规则，false不符合
     */
    public static final String NOTPASSWORDROLE = "false";
    
    /**
     * 操作日志，新增
     */
    public static final String LOG_CONTENT_INSERT = "新增";
    
    /**
     * 操作日志，修改
     */
    public static final String LOG_CONTENT_UPDATE = "修改";
    
    /**
     * 操作日志，删除
     */
    public static final String LOG_CONTENT_DELETE = "删除";
    
    /**
     * 操作日志，禁用
     */
    public static final String LOG_CONTENT_DISABLE = "禁用";
    
    /**
     * 操作日志，启用
     */
    public static final String LOG_CONTENT_ENABLE = "启用";
    
    /**
     * 操作日志，分配
     */
    public static final String LOG_CONTENT_ASSIGN = "分配";
    
    /**
     * 操作日志，关闭
     */
    public static final String LOG_CONTENT_CLOSE = "关闭";
    
    /**
     * 操作日志，导出
     */
    public static final String LOG_CONTENT_EXPORT = "导出";
    
    /**
     * 操作日志，复制
     */
    public static final String LOG_CONTENT_COPY = "复制";
    
    /**
     * 删除判断，不可删除-false
     */
    public static final String FALSE = "false";
    
    /**
     * 删除判断，可删除-true
     */
    public static final String TRUE = "true";
    
    /**
     * 登录状态：LOGIN_STATUS 登录中
     */
    public static final String LOGIN_STATUS_TRUE = "1";
    
    /**
     * 登录状态：LOGIN_STATUS 已注销
     */
    public static final String LOGIN_STATUS_FALSE = "0";
    
    /**
     * 注销原因：LOGOFF_TAG 正常注销
     */
    public static final String LOGOFF_TAG_NORMAL = "1";
    
    /**
     * 注销原因：LOGOFF_TAG session过期
     */
    public static final String LOGOFF_TAG_EXPIRE = "2";
    
    /**
     * 注销原因：LOGOFF_TAG 账号二次登录踢出
     */
    public static final String LOGOFF_TAG_DISPLACE = "3";
    
    /**
     * 注销原因：LOGOFF_TAG 管理界面踢出
     */
    public static final String LOGOFF_TAG_REJECT = "4";
}
