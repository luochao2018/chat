package com.web.chat.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Properties文件操作
 */
public class PropertiesUtil {
    private static Logger logger = LoggerFactory.getLogger(Object.class);
    private String propertisPath;
    private boolean isFileName;

    public PropertiesUtil(String propertisPath, boolean isFileName) {
        this.propertisPath = propertisPath;
        this.isFileName = isFileName;
    }

    /**
     * 赋值
     *
     * @param key      属性
     * @param value    值
     * @param isAppend 是否追加 //true表示追加打开
     */
    public void setValue(String key, String value, boolean isAppend) {
        Properties properties = new Properties();
        try {
            FileOutputStream fileOutputStream = null;
            if (this.isFileName) {
                String path = this.getClass().getClassLoader().getResource(this.propertisPath).getPath();
                fileOutputStream = new FileOutputStream(path, isAppend);
            } else {
                fileOutputStream = new FileOutputStream(this.propertisPath, isAppend);
            }
            properties.setProperty(key, value);
            propertiesType(properties, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            logger.info(e.toString());
        }
    }

    /**
     * 通过对象获取值
     *
     * @param key 参数
     * @return
     */
    public String getValue(Object key) {
        Properties properties = new Properties();
        try {
            InputStream inputStream = null;
            if (this.isFileName) {
                inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(this.propertisPath);
            } else {
                inputStream = new FileInputStream(this.propertisPath);
            }
            propertiesType(properties, inputStream);
        } catch (IOException e) {
            logger.info(e.toString());
        }
        return properties.getProperty((String) key);
    }

    /**
     * 赋值
     *
     * @param path       路径
     * @param key        属性
     * @param value      值
     * @param isAppend   是否插入
     * @param isFileName 是否为文件名
     */
    public static void setValue(String path, String key, String value, boolean isAppend, boolean isFileName) {
        PropertiesUtil propertiesUtil = new PropertiesUtil(path, isFileName);
        propertiesUtil.setValue(key, value, isAppend);
    }

    /**
     * 获取值
     *
     * @param path       路径
     * @param key        属性
     * @param isFileName 是否为文件名
     * @return
     */
    public static String getValue(String path, String key, boolean isFileName) {
        PropertiesUtil propertiesUtil = new PropertiesUtil(path, isFileName);
        return propertiesUtil.getValue(key);
    }

    /**
     * 文件类型
     *
     * @param properties
     * @param inputStream
     */
    private void propertiesType(Properties properties, InputStream inputStream) {
        String suffix = this.propertisPath.substring(this.propertisPath.lastIndexOf(".") + 1);
        try {
            switch (suffix.toLowerCase()) {
                case "properties":
                    properties.load(inputStream);
                    break;
                case "xml":
                    properties.loadFromXML(inputStream);
                    break;
                default:
                    properties.load(inputStream);
            }
        } catch (Exception e) {
            logger.info(e.toString());
        }
    }

    /**
     * 文件类型
     *
     * @param properties
     * @param fileOutputStream
     */
    private void propertiesType(Properties properties, FileOutputStream fileOutputStream) {
        String suffix = this.propertisPath.substring(this.propertisPath.lastIndexOf(".") + 1);
        try {
            switch (suffix.toLowerCase()) {
                case "properties":
                    properties.store(fileOutputStream, "The New properties file");
                    break;
                case "xml":
                    properties.storeToXML(fileOutputStream, "The New properties file");
                    break;
                default:
                    properties.store(fileOutputStream, "The New properties file");
            }
        } catch (Exception e) {
            logger.info(e.toString());
        }
    }

//    public static void main(String[] args) {
//        //测试PropertiesUtil
////        String path = FileUtil.getFilePathByName("application.properties");
//        String path = FileUtil.getFilePathByName("aa.xml");
//        System.out.println(path);
//
//        PropertiesUtil.setValue(path, "bb", "test22", true, false);
//
//        String name = PropertiesUtil.getValue(path, "test.ip", false);
//        String aa = PropertiesUtil.getValue(path, "bb", false);
//        System.out.println(name);
//        System.out.println(aa);
//    }
}