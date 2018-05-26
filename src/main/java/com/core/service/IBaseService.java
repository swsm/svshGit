/**
 * Service.java
 * Created at 2014-7-20
 * Created by Lee
 * Copyright (C) 2014 SHANGHAI BRODATEXT, All rights reserved.
 */
package com.core.service;

/**
 * 
 * <p>
 * ClassName: IBaseService
 * </p>
 * <p>
 * Description: 顶层服务接口
 * </p>
 * <p>
 * Author: Lee
 * </p>
 * <p>
 * Date: 2014-7-20
 * </p>
 */
public interface IBaseService {
    /**
     * 删除标记：未删除
     */
    public static final String DEL_FALSE = "0";

    /**
     * 删除标记：删除
     */
    public static final String DEL_TRUE = "1";

    /**
     * 主键属性名
     */
    public static final String PK = "id";

}
