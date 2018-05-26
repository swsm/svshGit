package com.core.tools.format;

import com.core.tools.JsonUtil;
import com.core.tools.PageInfo;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * ClassName: MesJsonUtil
 * </p>
 * <p>
 * Description: Json字符串操作Util类
 * </p>
 */
public class MesJsonUtil {
    /**
     * 
     * <p>
     * Description: List转jsonstr
     * </p>
     * 
     * @param list 需要转换的对象List
     * @param pageInfo 翻页消息
     * @param excludes 需要排除的属性
     * @return json格式的字符串
     */
    @SuppressWarnings({ "rawtypes" })
    public static String toJsonStr(List list, PageInfo pageInfo, Map<Class<?>, String[]> excludes) {
        if (excludes == null) {
            excludes = Collections.emptyMap();
        }
        String jsonStr = null;
        Map<String, Object> causeMaps;
        causeMaps = new HashMap<String, Object>();
        if (list == null || list.isEmpty()) {
            causeMaps.put("total", 0);
            causeMaps.put("rows", Collections.emptyList());
            jsonStr = JsonUtil.getJsonStringFromMap(causeMaps, excludes);
        } else {
            causeMaps.put("total", pageInfo.count);
            causeMaps.put("rows", list);
            jsonStr = JsonUtil.getJsonStringFromMap(causeMaps, excludes);
        }
        return jsonStr;
    }
}
