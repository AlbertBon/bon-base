package com.bon.common.util;


import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @program: 后台-基础项目
 * @description:
 * @author: Bon
 * @create: 2018-08-17 14:37
 **/
public class GeneratePropertiesUtils {
    private static final Map<String, String> properties = new HashMap<String, String>();

    static {
        try {
            Properties pps = new Properties();
            pps.load(GeneratePropertiesUtils.class.getClassLoader().getResourceAsStream("constant.properties"));
            //处理重复的值.
            for (Map.Entry<Object, Object> entry : pps.entrySet()) {
                properties.put(entry.getKey().toString().trim(), entry.getValue().toString().trim());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过key值去获取值.
     */
    public static String getValueByKey(String name) {
        return properties.get(name);

    }
}
