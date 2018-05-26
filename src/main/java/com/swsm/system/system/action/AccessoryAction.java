package com.swsm.system.system.action;

import com.core.action.BaseHandler;
import com.core.tools.JsonUtil;
import com.core.tools.MesBaseUtil;
import com.swsm.system.login.session.SessionManage;
import com.swsm.system.login.session.UserSession;
import com.swsm.system.system.model.*;
import com.swsm.system.system.service.IAccessoryService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>ClassName: AccessoryAction</p>
 * <p>Description: 附件Action</p>
 */
@Controller
@RequestMapping("/main/system")
public class AccessoryAction extends BaseHandler {
    
    /**
     * Accessoryservice
     */
    @Autowired
    @Qualifier("accessoryService")
    private IAccessoryService accessoryService;
    
    /**
     * 上传成功信息
     */
    private final static String UPLOAD_SUCCESS = "{success:true,mess:'文件上传成功!'}";
    /**
     * 上传成功信息
     */
    private final static String UPLOAD_FAILURE = "{success:false,mess:'文件上传失败!'}";
    /**
     * 上传提示
     * 信息
     */  
    private final static String FILE_NO = "{success:false,mess:'文件不存在!'}";
    /**
     * 上传提示信息
     */  
    private final static String FILE_YES = "{success:true,mess:'文件存在!'}";
    /**
     * 上传文件编码格式
     */  
    private final static String CONTENT_TYPE = "text/html;charset=utf-8";
    /**
     * 上传平台信息
     */ 
    private final static String APPLICATION = "application/octet-stream";

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(AccessoryAction.class);
    

    /**
     * <p>Description: 设置构造json字符串的属性排除数组</p>
     * @return 排除数组
     */
    protected Map<Class<?>, String[]> getJsonExcludedProperties() {
        Map<Class<?>, String[]> excludeMap;
        excludeMap = new HashMap<Class<?>, String[]>();
        excludeMap.put(User.class, new String[]{"roleList", "organList"});
        excludeMap.put(Role.class, new String[]{"userList"});
        excludeMap.put(Organ.class, new String[]{"children", "parentOrgan", "leaf", "userList"});
        return excludeMap;
    }
    
    /**
     * 
     * <p>Description: 操作的返回信息</p>
     * @param isSuccess 是否成功
     * @param message 返回消息
     * @return json格式字符串
     */
    protected String toJsonStrActionMessage(boolean isSuccess, String message) {
        return toJsonStrActionMessage(isSuccess, message, null, null, null);
    }
    
    /**
     * 
     * <p>Description: 操作的返回信息</p>
     * @param isSuccess 是否成功
     * @param message 返回消息
     * @param obj 返回对象
     * @param args 其他参数
     * @param excludes 转json字符串过程中要排除的属性，避免循环引用
     * @return json格式字符串
     */
    protected String toJsonStrActionMessage(boolean isSuccess, String message, DictIdx obj,
            Map<String , Object> args, Map<Class<?>, String[]> excludes) {
        Map<String, Object> causeMaps;
        causeMaps = new HashMap<String, Object>();
        causeMaps.put("success", isSuccess);
        causeMaps.put("message", message);
        causeMaps.put("resultObj", obj);
        if (args != null) {
            causeMaps.putAll(args);
        }
        if (excludes != null && excludes.size() > 0) {
          //如果excludes中包含有在转json过程中要排除掉的属性
            return JsonUtil.getJsonStringFromMap(causeMaps, excludes);
        } else {
          //如果excludes中不包含在转json过程中要排除掉的属性
            return JsonUtil.getJsonStringFromMap(causeMaps);
        }
    }
    
    /**
     * 测试上传文件  新框架
     * @param request 请求
     * @param response 回应
     * @throws Exception 异常
     */
    @RequestMapping(value = "/testUpload.mvc")
    @ResponseBody
    public void testUpload(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(CONTENT_TYPE);
        String result;
        if (!ServletFileUpload.isMultipartContent(request)) {
            result = UPLOAD_FAILURE;
            MesBaseUtil.writeJsonStr(response, this.toJsonStrActionMessage(true, result));
            return;
        }
        
        String name = null;
        DiskFileItemFactory factory;
        factory = new DiskFileItemFactory();
        factory.setSizeThreshold(4096);
        ServletFileUpload upload;
        upload = new ServletFileUpload(factory);
        //上传最大文件大小为4M
        upload.setSizeMax(4 * 1024 * 1024);
        Iterator<?> iter;
        iter = upload.parseRequest(request).iterator();
        
        while (iter.hasNext()) {
            FileItem item;
            item = (FileItem) iter.next();
            if (item.isFormField()) {
                name = item.getFieldName();
            } else {
                name = item.getName();
                String path;
                //path = getPath();
                path = "d://xx/";
                if (path == null || "".equals(path)) {
                } else {
                    //保存附件表信息
                    SessionManage sessionManage;
                    sessionManage = SessionManage.getSessionManage();
                    UserSession userSession;
                    userSession = sessionManage.getUserSession(request.getSession());
                    if ("success".equals(this.accessoryService.uploadAccessory(item, name.substring(
                            name.lastIndexOf(File.separator)
                            + 1, name.length()), path + name.substring(name.lastIndexOf(File.separator)
                            + 1, name.length()), System.currentTimeMillis() + "", userSession.getUserName()))) {
                        result = UPLOAD_SUCCESS;    
                        MesBaseUtil.writeJsonStr(response, this.toJsonStrActionMessage(true, result));
                        return;
                    } else {
                        result = UPLOAD_FAILURE;
                        MesBaseUtil.writeJsonStr(response, this.toJsonStrActionMessage(true, result));
                        return;
                    }
                    
                }
            }
        }
        result = "success";
        MesBaseUtil.writeJsonStr(response, this.toJsonStrActionMessage(true, result));
    }
    
    /**
     * <p>Description: 测试下载附件是否在服务器上存在</p>
     * @param request 请求
     * @param response 回应
     * @throws Exception 异常
     */
    @RequestMapping(value = "/testFileExists.mvc")
    @ResponseBody
    public void testFileExists(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String result;
        String fileName;
        fileName = request.getParameter("fileName");
        fileName = URLDecoder.decode(fileName, "utf-8");
        Accessory a;
        a = this.accessoryService.getAccessoryByAccessoryName(fileName);
        if (a == null) {
            //要下载的文件不存在
            result = "fileNotExists";
            MesBaseUtil.writeJsonStr(response, this.toJsonStrActionMessage(true, result));
            return;
        }
        result = "success";
        MesBaseUtil.writeJsonStr(response, this.toJsonStrActionMessage(true, result));
    }
    
    /**
     * <p>Description: 测试下载附件 新框架</p>
     * @param request 请求
     * @param response 回应
     * @throws Exception 异常
     */
    @RequestMapping(value = "/testDown.mvc")
    @ResponseBody
    public void testDown(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(APPLICATION);
        String fileName;
        fileName = request.getParameter("fileName");
        fileName = URLDecoder.decode(fileName, "utf-8");
        Accessory a;
        a = this.accessoryService.getAccessoryByAccessoryName(fileName);
        String path = a.getAccessoryPath();
        path = path.replace("%20", " ");
        File file;
        file = new File(path);
        
        // 清空response
        response.reset();
        
        // 设置response的Header
        response.addHeader("Content-Disposition", "attachment;filename="
                + new String(fileName.getBytes("utf-8"), "iso8859-1"));
        response.addHeader("Content-Length", "" + file.length());
        
        //以流的形式下载文件
        InputStream fis;
        fis = new BufferedInputStream(new FileInputStream(path));
        byte[] buffer;
        buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();
        
        OutputStream toClient;
        toClient = new BufferedOutputStream(response.getOutputStream());
        toClient.write(buffer);
        toClient.flush();
        toClient.close();
    }
}
