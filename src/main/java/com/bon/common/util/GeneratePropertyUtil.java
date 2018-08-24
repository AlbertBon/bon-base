package com.bon.common.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @program: bon基础项目
 * @description: 常量值获取
 * @author: Bon
 * @create: 2018-05-16 12:41
 **/
public class GeneratePropertyUtil {
    private static final MyLog log = MyLog.getLog(GeneratePropertyUtil.class);
    private static Properties props;

    static {
        loadProps();
    }

    synchronized static private void loadProps() {
        props = new Properties();
        InputStream in = null;
        try {
            /*第一种，通过类加载器进行获取properties文件流*/
            in = GeneratePropertyUtil.class.getClassLoader().getResourceAsStream("constant.properties");
            /*第二种，通过类进行获取properties文件流*/
            //in = PropertyUtil.class.getResourceAsStream("/constant.properties");
            props.load(in);
        } catch (FileNotFoundException e) {
            log.error("constant.properties文件未找到");
        } catch (IOException e) {
            log.error("出现IOException");
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
                log.error("constant.properties文件流关闭出现异常");
            }
        }
    }

    public static String getProperty(String key) {
        if (null == props) {
            loadProps();
        }
        return props.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        if (null == props) {
            loadProps();
        }
        return props.getProperty(key, defaultValue);
    }
}
