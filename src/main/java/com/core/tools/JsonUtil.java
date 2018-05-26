package com.core.tools;

import com.alibaba.fastjson.JSON;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 提供不同的结构类型之间的数据转换成JSON (XML,MAP,POJO ) -- >JSON
 * 
 * @author Administrator
 * 
 */
public class JsonUtil {

    /**
     * json字符串的格式化
     * 
     * @json 需要被格式化的json字符串
     * @return 格式化后的字符串
     */
    public static String formatJson(String json) {
        String fillStringUnit = "	";
        if (json == null || json.trim().length() == 0) {
            return null;
        }

        int fixedLenth = 0;
        List<String> tokenList = new ArrayList<String>();
        {
            String jsonTemp = json;
            //预读取 
            while (jsonTemp.length() > 0) {
                String token = getToken(jsonTemp);
                jsonTemp = jsonTemp.substring(token.length());
                token = token.trim();
                tokenList.add(token);
            }
        }

        for (int i = 0; i < tokenList.size(); i++) {
            String token = tokenList.get(i);
            int length = token.getBytes().length;
            if (length > fixedLenth && i < tokenList.size() - 1 && tokenList.get(i + 1).equals(":")) {
                fixedLenth = length;
            }
        }

        StringBuilder buf = new StringBuilder();
        int count = 0;
        for (int i = 0; i < tokenList.size(); i++) {

            String token = tokenList.get(i);

            if (token.equals(",")) {
                buf.append(token);
                doFill(buf, count, fillStringUnit);
                continue;
            }
            if (token.equals(":")) {
                buf.append(" ").append(token).append(" ");
                continue;
            }
            if (token.equals("{")) {
                String nextToken = tokenList.get(i + 1);
                if (nextToken.equals("}")) {
                    i++;
                    buf.append("{ }");
                } else {
                    count++;
                    buf.append(token);
                    doFill(buf, count, fillStringUnit);
                }
                continue;
            }
            if (token.equals("}")) {
                count--;
                doFill(buf, count, fillStringUnit);
                buf.append(token);
                continue;
            }
            if (token.equals("[")) {
                String nextToken = tokenList.get(i + 1);
                if (nextToken.equals("]")) {
                    i++;
                    buf.append("[ ]");
                } else {
                    count++;
                    buf.append(token);
                    doFill(buf, count, fillStringUnit);
                }
                continue;
            }
            if (token.equals("]")) {
                count--;
                doFill(buf, count, fillStringUnit);
                buf.append(token);
                continue;
            }

            buf.append(token);
            //左对齐 
            if (i < tokenList.size() - 1 && tokenList.get(i + 1).equals(":")) {
                int fillLength = fixedLenth - token.getBytes().length;
                if (fillLength > 0) {
                    for (int j = 0; j < fillLength; j++) {
                        buf.append(" ");
                    }
                }
            }
        }
        return buf.toString();
    }

    /**
     * 
     * @param json
     * @return
     */
    private static String getToken(String json) {
        StringBuilder buf = new StringBuilder();
        boolean isInYinHao = false;
        while (json.length() > 0) {
            String token = json.substring(0, 1);
            json = json.substring(1);

            if (!isInYinHao
                    && (token.equals(":") || token.equals("{") || token.equals("}") || token.equals("[")
                            || token.equals("]") || token.equals(","))) {
                if (buf.toString().trim().length() == 0) {
                    buf.append(token);
                }

                break;
            }

            if (token.equals("\\")) {
                buf.append(token);
                buf.append(json.substring(0, 1));
                json = json.substring(1);
                continue;
            }
            if (token.equals("\"")) {
                buf.append(token);
                if (isInYinHao) {
                    break;
                } else {
                    isInYinHao = true;
                    continue;
                }
            }
            buf.append(token);
        }
        return buf.toString();
    }

    /**
     * @param buf string
     * @param count 个数
     * @param fillStringUnit 填充字符串
     */
    private static void doFill(StringBuilder buf, int count, String fillStringUnit) {
        buf.append("\n");
        for (int i = 0; i < count; i++) {
            buf.append(fillStringUnit);
        }
    }

    /**
     * 将Java对象转换为JSON格式的字符串
     * 
     * @param javaObj POJO,例如日志的model
     * @return JSON格式的String字符串
     */
    public static String getJsonStringFromJavaPOJO(Object javaObj) {
        return JSON.toJSONString(javaObj);
    }

    /**
     * 将Map转换为JSON字符串
     * 
     * @param map
     * @return JSON字符串
     */
    public static String getJsonStringFromMap(Map<?, ?> map) {
        String json = JSON.toJSONString(map, new ComplexPropertyPreFilter());
        return json;
    }

    /**
     * 将Map转换为JSON字符串
     * 
     * @param map
     * @param excludes 需要排除的属性
     * @return JSON字符串
     */
    public static String getJsonStringFromMap(Map<?, ?> map, Map<Class<?>, String[]> excludes) {
        ComplexPropertyPreFilter filter = new ComplexPropertyPreFilter();

        filter.setExcludes(excludes);

        return JSON.toJSONString(map, filter);
    }

    /**
     * 排除bean之间的关系字段
     * 
     * @param clazz
     * @return
     */
    public static String[] getExcludeFields(Class clazz) {
        Set<String> list = new HashSet<String>();
        list.add("handler");
        list.add("hibernateLazyInitializer");
        for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {

            Field[] fields = superClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.getAnnotation(OneToOne.class) != null || field.getAnnotation(OneToMany.class) != null
                        || field.getAnnotation(ManyToOne.class) != null
                        || field.getAnnotation(ManyToMany.class) != null) {
                    list.add(field.getName());
                }
            }
        }
        return list.toArray(new String[list.size()]);
    }

}
