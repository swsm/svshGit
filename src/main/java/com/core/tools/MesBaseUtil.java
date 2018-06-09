package com.core.tools;

import com.core.entity.BaseModel;
import com.core.service.impl.EntityServiceImpl;
import com.core.tools.file.IOUtil;
import com.core.tools.format.StringHelper;
import com.swsm.constant.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;


/**
 * <p>
 * ClassName: BaseUtil
 * </p>
 * <p>
 * Description: 基础工具类
 * </p>
 */
public class MesBaseUtil {
    /**
     * 日志信息
     */
    private static Logger logger = LoggerFactory.getLogger(MesBaseUtil.class);

    /**
     * 
     * <p>
     * Description: 判断目标值是否为null
     * </p>
     * 
     * @param targetValue 目标值
     * @return 返回是否为
     */
    public static boolean isEmpty(String targetValue) {
        return isEmpty(targetValue, true);
    }

    /**
     * 
     * <p>
     * Description: 判断目标值是否为null
     * </p>
     * 
     * @param targetValue 目标值
     * @param isExact true：精确；false：包括all与blank
     * @return 返回是否为
     */
    public static boolean isEmpty(String targetValue, boolean isExact) {
        if (isExact) {
            if (targetValue == null) {
                return true;
            }
        }
        boolean isAll;
        isAll = targetValueIsAll(targetValue);
        boolean isBlank;
        isBlank = targetValueIsBlank(targetValue);
        return isAll && isBlank;
    }

    /**
     * 
     * <p>
     * Description: 判断targetValue是否为all
     * </p>
     * 
     * @param targetValue 目标参数
     * @return true：是all，false：不是all
     */
    public static boolean targetValueIsAll(String targetValue) {
        if (targetValue == null) {
            return false;
        }
        return "all".equalsIgnoreCase(targetValue);
    }

    /**
     * 
     * <p>
     * Description: 判断targetValue是否为“”
     * </p>
     * 
     * @param targetValue 目标参数
     * @return true：是“”，false：不是“”
     */
    public static boolean targetValueIsBlank(String targetValue) {
        if (targetValue == null) {
            return false;
        }
        return "".equals(targetValue.trim());
    }

    /**
     * 
     * <p>
     * Description: 根据输入的sql列以及该列值的集合,whrValues中不允许有特殊字符，而且不能超过1000个值
     * </p>
     * 
     * @param whrField sql列
     * @param whrValues 该列值的集合
     * @return sql条件语句
     */
    public static String createInWhr(String whrField, String[] whrValues) {
        if (whrField == null || whrValues == null) {
            return " and 1=1 ";
        }
        if (whrValues.length == 0) {
            return " and 1=2 ";
        }
        StringBuilder builder;
        builder = new StringBuilder();
        for (String value : whrValues) {
            builder.append("'").append(value).append("'").append(",");
        }
        builder.delete(builder.length() - 1, builder.length());
        return " and " + whrField + " in (" + builder.toString() + ")";
    }

    /**
     * <p>
     * Description: 根据输入的sql列以及该列值的集合,whrValues中不允许有特殊字符，而且不能超过1000个值
     * </p>
     * 
     * @param whrField sql列
     * @param whrValues 该列值的集合
     * @return sql条件语句
     */
    public static String createNotInWhr(String whrField, String[] whrValues) {
        if (whrField == null || whrValues == null) {
            return " and 1=1 ";
        }
        if (whrValues.length == 0) {
            return " and 1=2 ";
        }
        StringBuilder builder;
        builder = new StringBuilder();
        for (String value : whrValues) {
            builder.append("'").append(value).append("'").append(",");
        }
        builder.delete(builder.length() - 1, builder.length());
        return " and " + whrField + " not in (" + builder.toString() + ")";
    }

    /**
     * 
     * <p>
     * Description: 设置对象默认值
     * </p>
     * 
     * @param baseModel 基础对象
     * @param isAdd 是否新增
     * @param userInfo 挡墙用户信息
     */
    public static void setDefaultFiledsBaseModel(BaseModel baseModel, String[] userInfo, boolean isAdd) {
        baseModel.setDelFlag(EntityServiceImpl.DEL_FALSE);
        if (isAdd) {
            baseModel.setCreateDate(new Date());
            baseModel.setCreateUser(userInfo[1]);
        } else {
            baseModel.setUpdateDate(new Date());
            baseModel.setUpdateUser(userInfo[1]);
        }
    }

    /**
     * 
     * <p>
     * Description: 把集合信息写入到response中
     * </p>
     * 
     * @param response 响应
     * @param c 需要写入的集合
     * @throws Exception 异常
     */
    @SuppressWarnings({ "unused", "rawtypes" })
    public static void writeJsonDataToResponse(HttpServletResponse response, Collection c) throws Exception {
        if (c instanceof List) {
            if (c == null) {
                c = Collections.emptyList();
            }
        }
        if (c instanceof Set) {
            if (c == null) {
                c = Collections.emptySet();
            }
        }
        int size;
        size = c.size();
        Map<String, Object> map;
        map = new HashMap<String, Object>();
        map.put(CommonConstants.ROWS, c);
        map.put(CommonConstants.TOTAL, c.size());
        String jsonStr;
        jsonStr = JsonUtil.getJsonStringFromMap(map);
        writeJsonStr(response, jsonStr);
    }

    /**
     * 
     * <p>
     * Description: 把jsonStr信息写入到response中
     * </p>
     * 
     * @param response 响应
     * @param jsonStr 需要写入的集合
     */
    public static void writeJsonStr(HttpServletResponse response, String jsonStr) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.write(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtil.closeWriter(out);
        }
    }

    public static boolean downFile(HttpServletResponse response, String filePath) {
        File file;
        file = new File(filePath);
        if (!file.exists()) {
            logger.info("filePath({}) is not exists!", filePath);
            return false;
        }
        String fileName = StringHelper.parsorUtf8Str(filePath);
        response.setContentType("application/octet-stream");
        response.reset();
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.addHeader("Content-Length", "" + file.length());
        InputStream fis = null;
        OutputStream toClient = null;
        try {
            fis = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer;
            buffer = new byte[fis.available()];
            fis.read(buffer);
            toClient = new BufferedOutputStream(response.getOutputStream());
            toClient.write(buffer);
            toClient.flush();
            return true;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return false;
        } finally {
            IOUtil.closeInputStream(fis);
            IOUtil.closeOutputStream(toClient);
        }

    }
    
    /**
	 * 判断key中的value是否为null
	 * @param key
	 * @param queryMap
	 * @return
	 */
	public static boolean isNotNull(String key,Map<String,Query> queryMap){
		if(key==null || queryMap==null){
			return false;
		}
		Query query= queryMap.get(key);
		if(query==null){
			return false;
		}
		Object obj=query.getValue();
		if(obj==null){
			return false;
		}
		return true;
	}
	
	
	 /**
     * 在queryMap获取key对应的值
     * @param key
     * @param queryMap
     * @return
     */
    public static String getValue(String key,Map<String,Query> queryMap){
    	if(queryMap==null || key==null){
    		return null;
    	}
    	Query query = queryMap.get(key);
    	if(query==null){
    		return null;
    	}
    	Object valueObj = query.getValue();
    	if(valueObj==null){
    		return null;
    	}
    	return valueObj.toString();
    }

}
