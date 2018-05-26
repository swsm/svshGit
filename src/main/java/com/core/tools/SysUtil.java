package com.core.tools;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;


public class SysUtil {
    private static char serverId;
    private static Object idLock = new Object();

    //ADDED 20150309 生成token zhangwei begin
    public static String getUUID() {
        String s = UUID.randomUUID().toString();
        //去掉“-”符号 
        return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
    }

    private static char intToChar(int val) {
        if (val < 10)
            return (char) (val + 48);
        else
            return (char) (val + 55);
    }

    private static String numToString(long num) {
        char[] buf = new char[12];
        int charPos = 12;
        long val;

        buf[0] = serverId;
        while ((val = num / 36) > 0) {
            buf[--charPos] = intToChar((int) (num % 36));
            num = val;
        }
        buf[--charPos] = intToChar((int) (num % 36));
        return new String(buf);
    }

    public static String getShortError(Throwable e) {
        Throwable cause = e, c = e;

        while (c != null) {
            cause = c;
            c = c.getCause();
        }
        String message = cause.getMessage();
        if (StringUtil.isEmpty(message))
            message = cause.toString();
        return StringUtil.toLine(message);
    }

    public static void executeMethod(String classMethodName, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        int pos = classMethodName.lastIndexOf('.');
        String className, methodName;

        if (pos == -1) {
            className = "";
            methodName = classMethodName;
        } else {
            className = classMethodName.substring(0, pos);
            methodName = classMethodName.substring(pos + 1);
        }

        Class<?> cls = Class.forName(className);
        //改为调用实例化的方法,不采用静态方法 ,并且从spring获取实例
        try {
            Object obj = SpringUtil.getBean(request, cls.getSimpleName());

            if (classMethodName.indexOf("com.webbuilder") < 0) {
                cls.getMethod("init", HttpServletRequest.class, HttpServletResponse.class, Object.class, String.class)
                        .invoke(obj, request, response, obj, methodName);

            } else {
                cls.getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class).invoke(obj, request,
                        response);
            }

        } catch (NoSuchBeanDefinitionException e) {
            if (classMethodName.indexOf("com.webbuilder") < 0) {
                cls.getMethod("init", HttpServletRequest.class, HttpServletResponse.class, Object.class, String.class)
                        .invoke(cls.newInstance(), request, response, cls.newInstance(), methodName);
            } else {
                cls.getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class).invoke(
                        cls.newInstance(), request, response);
            }
        }
        //改为调用实例化的方法,不采用静态方法 
        //		cls.getMethod(methodName, HttpServletRequest.class,
        //				HttpServletResponse.class).invoke(cls.newInstance(), request, response);
        //		cls.getMethod(methodName, HttpServletRequest.class,
        //                HttpServletResponse.class).invoke(cls, request, response);
    }

    public static void executeMethod(String classMethodName) throws Exception {
        if (StringUtil.isEmpty(classMethodName))
            return;
        int pos = classMethodName.lastIndexOf('.');
        String className, methodName;

        if (pos == -1) {
            className = "";
            methodName = classMethodName;
        } else {
            className = classMethodName.substring(0, pos);
            methodName = classMethodName.substring(pos + 1);
        }
        Class<?> cls = Class.forName(className);
        cls.getMethod(methodName).invoke(cls);
    }

    public static int isToOs(InputStream is, OutputStream os) throws Exception {
        byte buf[] = new byte[8192];
        int len, size = 0;

        while ((len = is.read(buf)) > 0) {
            os.write(buf, 0, len);
            size += len;
        }
        return size;
    }

    public static ByteArrayInputStream getBIS(InputStream is) throws Exception {
        if (is == null)
            return null;
        if (is instanceof ByteArrayInputStream)
            return (ByteArrayInputStream) is;
        ByteArrayOutputStream bos;
        try {
            bos = new ByteArrayOutputStream();
            SysUtil.isToOs(is, bos);
        } finally {
            is.close();
        }
        return new ByteArrayInputStream(bos.toByteArray());
    }

    public static String readString(Reader reader) throws Exception {
        char buf[] = new char[8192];
        StringBuilder sb = new StringBuilder();
        int len;

        while ((len = reader.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    public static void closeInputStream(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (Throwable e) {
            }
        }
    }

    public static void error(String msg) throws Exception {
        throw new Exception(msg);
    }
}
