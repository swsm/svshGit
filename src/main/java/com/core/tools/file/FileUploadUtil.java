package com.core.tools.file;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * ClassName: FileUploadUtil
 * </p>
 * <p>
 * Description: 文件上传工具类
 * </p>
 */
public class FileUploadUtil {
    /**
     * 日志信息
     */
    private static Logger logger = LoggerFactory.getLogger(FileUploadUtil.class);
    /**
     * 默认上传文件大小为20M
     */
    private final static int DEFAULT_MAX_FILE_SIZE = 20 * 1024 * 1024;
    /**
     * 内存缓冲区
     */
    private final static int DEFAULT_BUFFER_SIZE = 4096;

    /**
     * 
     * <p>
     * Description: 上传文件到指定位置
     * </p>
     * 
     * @param request 文件数据流
     * @param dir 上传路径目录
     * @param fileName 文件名称
     * @return 是否上传成功
     * @throws FileUploadException 文件上传异常
     * @throws IOException IO异常
     */
    public static boolean uploadFile(HttpServletRequest request, String dir, String fileName)
            throws FileUploadException, IOException {
        boolean isValid = FileUploadUtil.isValidFile(dir, fileName);
        if (!isValid) {
            return false;
        }
        String filePath = dir + File.pathSeparator + fileName;
        File file = new File(filePath);
        FileWriter fielWriter = new FileWriter(file);
        try {
            InputStream[] ins = FileUploadUtil.getUploadInputStream(request);
            int len = ins.length;
            if (len == 0) {
                logger.info("not upload file!");
                return false;
            } else if (len == 1) {
                InputStream in = ins[0];
                int c;
                while ((c = in.read()) != -1) {
                    fielWriter.write(c);
                }
            } else {
                logger.info("load by zip!");
                String tmpDir = System.getProperty("java.io.tmpdir");
                String[] fileNames = new String[len];
                File[] tempFiles = new File[len];
                int index = 0;
                for (InputStream in : ins) {
                    fileNames[index] = tmpDir + File.pathSeparator + "temp-" + index;
                    tempFiles[index] = new File(fileNames[index]);
                    FileWriter tempFileWriter = new FileWriter(tempFiles[index]);
                    int c;
                    while ((c = in.read()) != -1) {
                        tempFileWriter.write(c);
                    }
                    index++;
                }
                ZipFileUtil.zipFiles(filePath, tempFiles);
            }
            return true;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw e;
        } finally {
            if (fielWriter != null) {
                fielWriter.close();
            }
        }
    }

    /**
     * 
     * <p>
     * Description: 判断文件路径是否有效
     * </p>
     * 
     * @param dir 文件目录
     * @param fileName 文件名称
     * @return true：合法；false：不合法
     * @throws IOException IO异常
     */
    public static boolean isValidFile(String dir, String fileName) throws IOException {
        if (dir == null || fileName == null) {
            logger.error("dir:{},fileName:{}", dir, fileName);
            return false;
        }
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            logger.debug("dir({}) is not exists!", dir);
            dirFile.mkdir();
        }
        String filePath = dir + File.pathSeparator + fileName;
        File file = new File(filePath);
        if (!file.exists()) {
            logger.debug("file ({}) is not exists!", filePath);
            file.createNewFile();
        }
        if (file.isDirectory()) {
            logger.debug("file ({}) is dir!", filePath);
            return false;
        }
        return true;
    }

    /**
     * 
     * <p>
     * Description: 根据上传文件获取文件数据流
     * </p>
     * 
     * @param request 上传数据
     * @return 输入流数组
     * @throws FileUploadException 文件上传异常
     * @throws IOException IO异常
     */
    public static InputStream[] getUploadInputStream(HttpServletRequest request) throws FileUploadException,
            IOException {
        ServletFileUpload upload = FileUploadUtil.createServletFileUpload();
        return FileUploadUtil.getUploadInputStream(request, upload);
    }

    /**
     * 
     * <p>
     * Description: 根据上传文件获取文件数据流
     * </p>
     * 
     * @param request 上传数据
     * @param maxSize 上传文件的size
     * @return 输入流数组
     * @throws FileUploadException 文件上传异常
     * @throws IOException IO异常
     */
    public static InputStream[] getUploadInputStream(HttpServletRequest request, int maxSize)
            throws FileUploadException, IOException {
        ServletFileUpload upload = FileUploadUtil.createServletFileUpload(maxSize, DEFAULT_BUFFER_SIZE);
        return FileUploadUtil.getUploadInputStream(request, upload);
    }

    /**
     * 
     * <p>
     * Description: 根据上传文件获取文件数据流
     * </p>
     * 
     * @param request 上传数据
     * @param upload 上传文件对象
     * @return 输入流数组
     * @throws FileUploadException 文件上传异常
     * @throws IOException IO异常
     */
    @SuppressWarnings("unchecked")
    private static InputStream[] getUploadInputStream(HttpServletRequest request, ServletFileUpload upload)
            throws FileUploadException, IOException {
        Iterator<FileItem> iterator;
        iterator = upload.parseRequest(request).iterator();
        List<InputStream> insList = new LinkedList<InputStream>();
        while (iterator.hasNext()) {
            FileItem fileItem;
            fileItem = iterator.next();
            insList.add(fileItem.getInputStream());
        }
        return insList.toArray(new InputStream[0]);
    }

    /**
     * 
     * <p>
     * Description: 创建文件上传对象
     * </p>
     * 
     * @param maxSize 文件大小上限（byte）
     * @param bufferSize 缓冲区大小（byte）
     * @return ServletFileUpload 文件上传对象
     */
    private static ServletFileUpload createServletFileUpload(int maxSize, int bufferSize) {
        DiskFileItemFactory factory;
        factory = new DiskFileItemFactory();
        factory.setSizeThreshold(bufferSize);
        ServletFileUpload upload;
        upload = new ServletFileUpload(factory);
        upload.setSizeMax(maxSize);
        logger.debug("maxSize:{},bufferSize:{},createServletFileUpload ok!", maxSize, bufferSize);
        return upload;
    }

    /**
     * 
     * <p>
     * Description: Description: 创建文件上传对象
     * </p>
     * 
     * @return ServletFileUpload 文件上传对象
     */
    private static ServletFileUpload createServletFileUpload() {
        return FileUploadUtil.createServletFileUpload(DEFAULT_MAX_FILE_SIZE, DEFAULT_BUFFER_SIZE);
    }

}
