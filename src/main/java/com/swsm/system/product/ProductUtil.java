package com.swsm.system.product;

import com.core.exception.BaseException;
import com.core.tools.XmlReader;
import com.swsm.system.main.vo.ProductModel;
import org.dom4j.Node;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


/**
 * 
 * <p>
 * ClassName: ProductUtil
 * </p>
 * <p>
 * Description: 产品工具类
 * </p>
 */
public class ProductUtil {

    /**
     * 
     * <p>
     * Description: 获取产品信息
     * </p>
     * 
     * @return ProductModel
     * @throws BaseException 异常
     */
    public static ProductModel getProduct() throws BaseException {
        String sysHomePath;
        sysHomePath = SysHome.getSysHomePath();
        if (sysHomePath == null) {
            return null;
        }
        String fileName;
        fileName = sysHomePath + File.separator + "product.xml";
        String printName;
        printName = XmlReader.getValueByPath(fileName, "/product/vendor/print-name");
        String printLogoUrl;
        printLogoUrl = XmlReader.getValueByPath(fileName, "/product/vendor/printLogo-url");
        String vendorName;
        vendorName = XmlReader.getValueByPath(fileName, "/product/vendor/vendor-name");
        String logoUrl;
        logoUrl = XmlReader.getValueByPath(fileName, "/product/vendor/logo-url");
        String sysName;
        sysName = XmlReader.getValueByPath(fileName, "/product/sys/sys-name");
        String sysVersion;
        sysVersion = XmlReader.getValueByPath(fileName, "/product/sys/sys-version");
        String sysMaking;
        sysMaking = XmlReader.getValueByPath(fileName, "/product/sys/sys-make");
        String erpFlag;
        erpFlag = XmlReader.getValueByPath(fileName, "/product/erp/erp-flag");
        ProductModel product;
        product = new ProductModel();
        product.setPrintLogoUrl(printLogoUrl);
        product.setPrintName(printName);
        product.setLogoUrl(logoUrl);
        product.setSysMaking(sysMaking);
        product.setSysName(sysName);
        product.setSysVersion(sysVersion);
        product.setVendorName(vendorName);
        product.setErpFlag(erpFlag);
        Node[] nodes;
        nodes = XmlReader.getNodesByPath(fileName, "/product/vars/var");
        Map<String, String> varMap;
        varMap = new HashMap<String, String>();
        for (Node node : nodes) {
            String varName;
            varName = node.valueOf("@name");
            String varValue;
            varValue = node.valueOf("@value");
            varMap.put(varName, varValue);
        }
        product.setVarMap(varMap);
        return product;
    }

}
