package com.swsm.system.action;

import com.core.action.BaseHandler;
import com.core.exception.BaseException;
import com.core.tools.JsonUtil;
import com.core.tools.MesBaseUtil;
import com.swsm.main.vo.ProductModel;
import com.swsm.product.ProductUtil;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.spi.XmlReader;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * ClassName: ErpAction
 * </p>
 * <p>
 * Description: ERP系统进行验证
 * </p>
 */
@Controller
@RequestMapping("/main/erpAction")
public class ErpAction extends BaseHandler {
    /**
     * mapList 数据集
     */
    public static Map<String, String> erpParamMap = new HashMap<String, String>();

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(ErpAction.class);

    /**
     * xml文件doc
     */
    private static Document xmlDoc;
    /**
     * 已经加载的文件
     */
    private static String filePath;
    /**
     * 设置文件缓存时间（s）
     */
    private static int cacheTime = 3600;
    /**
     * 文件加载时间
     */
    private static long initLoadTime;
    
    /**
     * 查询是否存在ERP系统
     * 
     * @param request 请求
     * @param response 回应
     * @throws Exception 异常
     */
    @RequestMapping(value = "/getErpFlag.mvc")
    @ResponseBody
    public void getErpFlag(HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean flag;
        flag = false;
        ProductModel p;
        p = ProductUtil.getProduct();
        if (p != null) {
            if (!StringUtils.isEmpty(p.getErpFlag())) {
                flag = Boolean.parseBoolean(p.getErpFlag());
            }
        }
        Map<String, Boolean> causeMaps;
        causeMaps = new HashMap<>();
        causeMaps.put("erpFlag", flag);
        MesBaseUtil.writeJsonStr(response, JsonUtil.getJsonStringFromMap(causeMaps));
    }
    
    /**
     * 
     * <p>
     * Description: TODO
     * </p>
     * 
     */
    public void initXml() {
        this.loadXmlDocument(); //加载配置文件
        Element erps;
        erps = xmlDoc.getRootElement(); //获得配置文件的根集合
        List<Element> opList;
        opList = erps.elements("page"); //获得二级菜单集合
        List<String> listField;
        for (Element element : opList) {
            listField = new ArrayList<>();
            List paramList;
            paramList = element.elements();
            for (Object o : paramList) {
                Element param;
                param = (Element) o;
                listField.add(param.attributeValue("field"));
            }
            logger.info("*******" + listField + "*******");
            ErpAction.erpParamMap.put(element.attributeValue("pageId"), StringUtils.join(listField.toArray(), ","));
        }

    }

    /**
     * 
     * <p>
     * Description: 按照页面进行必选字段的查询
     * </p>
     * 
     * @param pageId 用户页面id
     * @param request 用户请求
     * @param response 用户响应
     * @throws BaseException 异常
     */
    @RequestMapping(value = "/getErpRequiredField.mvc")
    @ResponseBody
    public void getErpRequiredField(String pageId, HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        Map<String, Object> rltMap;
        rltMap = new HashMap<>();
        boolean flag;
        flag = false;
        ProductModel p;
        p = ProductUtil.getProduct();
        if (p != null) {
            if (!StringUtils.isEmpty(p.getErpFlag())) {
                flag = Boolean.parseBoolean(p.getErpFlag());
            }
        }
        if (flag) {
            if (ErpAction.erpParamMap.isEmpty()) { //如果不存在进行初始化
                this.initXml();
                
            }  
        }
        rltMap.put("json", ErpAction.erpParamMap.get(pageId));
        rltMap.put("erpFlag", flag);
        MesBaseUtil.writeJsonStr(response, JsonUtil.getJsonStringFromMap(rltMap));
    }

    /**
     * <p>
     * Description: 加载ERP所需的参数
     * </p>
     * 
     */
    public void loadXmlDocument() {
        String xmlFilePath;
        xmlFilePath = this.getClass().getClassLoader().getResource("/").getPath(); //获得文件路径
        String temp;
        temp = xmlFilePath + File.separator + "erp-params.xml"; //临时文件名称
        File file;
        file = new File(temp);
        if (!file.exists() || !file.isFile()) {
            URL url;
            url = XmlReader.class.getResource(temp); //xml文件读取
            if (url == null) {
                throw new IllegalArgumentException(temp + " not find!"); //异常
            }
            temp = url.getFile();
        }
        long currentTime;
        currentTime = System.currentTimeMillis() / 1000; //读取时间
        if (xmlFilePath.equals(filePath) || (currentTime - initLoadTime) < cacheTime) {
            return;
        }
        SAXReader reader;
        reader = new SAXReader(); //
        try {
            xmlDoc = reader.read(temp);
            filePath = xmlFilePath;
            initLoadTime = System.currentTimeMillis() / 1000;
        } catch (DocumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
