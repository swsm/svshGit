package com.swsm.system.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 
 * <p>
 * ClassName: SysHome
 * </p>
 * <p>
 * Description: 系统参数
 * </p>
 */
public class SysHome {
    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(SysHome.class);
    /**
     * 设置的SYS-HOME
     */
    private static String sysHomePath;
    /**
     * 设置的SYS-HOME的名称
     */
    private static final String HOME_NAME = "SYS_HOME";

    /**
     * 
     * <p>
     * Description: 获取设置的系统变量
     * </p>
     * 
     * @return 设置的系统变量
     */
    public static String getSysHomePath() {
        if (sysHomePath == null) {
            Map<String, String> map;
            map = System.getenv();
            sysHomePath = map.get(HOME_NAME);
            logger.info("SYS_HOME:" + sysHomePath);
        }
        return sysHomePath;
    }

  

}
