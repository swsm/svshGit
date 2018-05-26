package com.core.tools.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * <p>
 * ClassName: ZipFileUtil
 * </p>
 * <p>
 * Description: ZIP压缩处理类
 * </p>
 */
public class ZipFileUtil {
    /**
     * 日志信息
     */
    private static Logger logger = LoggerFactory.getLogger(ZipFileUtil.class);
    /**
     * 
     * <p>Description: 文件压缩</p>
     * @param zipFileName 文件名称
     * @param files 需要压缩的文件
     * @throws IOException IO异常
     */
    public static void zipFiles(String zipFileName, File[] files) throws IOException {
        OutputStream ops = null;
        ops = new FileOutputStream(zipFileName);
        CheckedOutputStream cops = null;
        cops = new CheckedOutputStream(ops, new CRC32());
        ZipOutputStream zipOs;
        zipOs = new ZipOutputStream(cops);
        try {
            for (File file : files) {
                ZipFileUtil.writeFile(zipOs, file);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw e;
        } finally {
            if (zipOs != null) {
                zipOs.close();
            }
            if (cops != null) {
                cops.close();
            }
            if (ops != null) {
                ops.close();
            }
        }

    }

    /**
     * 
     * <p>Description: 把文件加入到zip输出流</p>
     * @param zipOs zip输出流
     * @param file 需要压缩的文件
     * @throws IOException IO异常
     */
    private static void writeFile(ZipOutputStream zipOs, File file) throws IOException {
        String fileName;
        fileName = file.getName();
        zipOs.putNextEntry(new ZipEntry(fileName));
        InputStream is = null;
        BufferedInputStream bis = null;
        try {
            is = new FileInputStream(file.getPath());
            bis = new BufferedInputStream(is);
            byte[] b = new byte[1024];
            while (bis.read(b) != -1) {
                zipOs.write(b);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw e;
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }
}
