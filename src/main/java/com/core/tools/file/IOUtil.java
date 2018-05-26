package com.core.tools.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * <p>
 * ClassName: IOUtil
 * </p>
 * <p>
 * Description: TODO
 * </p>
 */
public class IOUtil {
    /**
     * 日志信息
     */
    private static Logger logger = LoggerFactory.getLogger(IOUtil.class);

    /**
     * 
     * <p>
     * Description: 关闭InputStream
     * </p>
     * 
     * @param is inputStream
     */
    public static void closeInputStream(InputStream is) {
        try {
            if (is != null) {
                is.close();
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

    }

    /**
     * 
     * <p>
     * Description: 关闭OutputStream
     * </p>
     * 
     * @param os OutputStream
     */
    public static void closeOutputStream(OutputStream os) {
        try {
            if (os != null) {
                os.close();
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

    }

    /**
     * 
     * <p>
     * Description: 关闭Reader
     * </p>
     * 
     * @param reader Reader
     */
    public static void closeReader(Reader reader) {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

    }

    /**
     * 
     * <p>
     * Description: 关闭Writer
     * </p>
     * 
     * @param writer Writer
     */
    public static void closeWriter(Writer writer) {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

    }

}
