package com.core.tools;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * 
 * <p>
 * ClassName: XmlReader
 * </p>
 * <p>
 * Description: xml格式文件reader
 * </p>
 */
public class XmlReader {
    /**
     * 日志信息
     */
    private static Logger logger = LoggerFactory.getLogger(XmlReader.class);
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
     * 
     * <p>
     * Description: 根据xml文件路径加载文件信息. 检查文件格式，非法文件将抛出异常.
     * 文件查找方式，首先根据文件系统查找，如果没有找到，则从class-path中查找，仍未找到将抛出异常 同一文件缓存一个小时
     * </p>
     * 
     * @param xmlFilePath 文件路径
     * @throws IllegalArgumentException 参数非法异常
     */
    public static void loadXmlDocument(String xmlFilePath) throws IllegalArgumentException {
        if (xmlFilePath == null) {
            throw new IllegalArgumentException("xmlFilePath is null");
        }
        String temp = xmlFilePath;
        int index = xmlFilePath.lastIndexOf(".");
        if (index == -1) {
            temp += ".xml";
        }
        File file = new File(temp);
        if (!file.exists() || !file.isFile()) {
            URL url = XmlReader.class.getResource(temp);
            if (url == null) {
                throw new IllegalArgumentException(temp + " not find!");
            }
            temp = url.getFile();
        }
        long currentTime = System.currentTimeMillis() / 1000;
        if (xmlFilePath.equals(filePath) && (currentTime - initLoadTime) < cacheTime) {
            return;
        }
        SAXReader reader = new SAXReader();
        try {
            xmlDoc = reader.read(temp);
            filePath = xmlFilePath;
            initLoadTime = System.currentTimeMillis() / 1000;
        } catch (DocumentException e) {
            logger.error(e.getMessage(), e);
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * 
     * <p>
     * Description: 根据文件路径以及节点路径获取节点集合
     * </p>
     * 
     * @param xmlFilePath 文件路径
     * @param path 节点表达式
     * @return 节点集合
     * @throws IllegalArgumentException 参数非法异常
     */
    @SuppressWarnings("unchecked")
    public static Node[] getNodesByPath(String xmlFilePath, String path) throws IllegalArgumentException {
        loadXmlDocument(xmlFilePath);
        if (path == null) {
            throw new IllegalArgumentException("path is null");
        }
        List<Node> list = xmlDoc.selectNodes(path);
        if (list == null || list.isEmpty()) {
            return new Node[0];
        }
        return list.toArray(new Node[0]);
    }

    /**
     * 
     * <p>
     * Description: 根据文件路径以及节点路径获取节点所在值
     * </p>
     * 
     * @param xmlFilePath xml文件路径
     * @param path xml节点路径
     * @return 节点所在值
     * @throws IllegalArgumentException 参数异常
     */
    public static String getValueByPath(String xmlFilePath, String path) throws IllegalArgumentException {
        loadXmlDocument(xmlFilePath);
        if (path == null) {
            throw new IllegalArgumentException("path is null!");
        }
        Node node = xmlDoc.selectSingleNode(path);
        if (node == null) {
            throw new IllegalArgumentException("path[" + path + "] is error,is not the path!");
        }
        return node.getStringValue();
    }

   

}
