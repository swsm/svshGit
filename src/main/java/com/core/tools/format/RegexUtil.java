package com.core.tools.format;

/**
 * <p>
 * ClassName: RegexUtil
 * </p>
 * <p>
 * Description: 正则工具类
 * </p>
 */
public class RegexUtil {

    /**
     * <p>
     * Description: 判断字符串是否是由数字组成
     * </p>
     * 
     * @param string 字符串
     * @return true：字符串是否数字组成；false：数字不全是由数字组成
     */
    public static boolean isNum(String string) {
        return string.matches("^-?[0-9]+.?[0-9]*$");
    }

}
