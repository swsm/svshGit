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
        String sysHomePath = SysHome.getSysHomePath();
        if (sysHomePath == null) {
            return null;
        }
        String fileName = sysHomePath + File.separator + "product.xml";
        String printName = XmlReader.getValueByPath(fileName, "/product/vendor/print-name");
        String printLogoUrl = XmlReader.getValueByPath(fileName, "/product/vendor/printLogo-url");
        String vendorName = XmlReader.getValueByPath(fileName, "/product/vendor/vendor-name");
        String logoUrl = XmlReader.getValueByPath(fileName, "/product/vendor/logo-url");
        String sysName = XmlReader.getValueByPath(fileName, "/product/sys/sys-name");
        String sysVersion = XmlReader.getValueByPath(fileName, "/product/sys/sys-version");
        String sysMaking = XmlReader.getValueByPath(fileName, "/product/sys/sys-make");
        String erpFlag = XmlReader.getValueByPath(fileName, "/product/erp/erp-flag");
        ProductModel product = new ProductModel();
        product.setPrintLogoUrl(printLogoUrl);
        product.setPrintName(printName);
        product.setLogoUrl(logoUrl);
        product.setSysMaking(sysMaking);
        product.setSysName(sysName);
        product.setSysVersion(sysVersion);
        product.setVendorName(vendorName);
        product.setErpFlag(erpFlag);
        Node[] nodes = XmlReader.getNodesByPath(fileName, "/product/vars/var");
        Map<String, String> varMap = new HashMap<>();
        for (Node node : nodes) {
            varMap.put(node.valueOf("@name"), node.valueOf("@value"));
        }
        product.setVarMap(varMap);
        return product;
    }

}
