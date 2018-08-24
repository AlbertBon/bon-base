package com.bon.common.util;

import java.io.File;
import java.io.FileWriter;

/**
 * @program: 后台-基础项目
 * @description: 文件工具类
 * @author: Bon
 * @create: 2018-08-17 14:34
 **/
public class GenerateFileUtils {
    /**
     * 把生成的文件都保存.
     *
     * @param path
     * @param data
     */
    public static void save(String path, String data) {
        /*转换文本换行为";"符号*/
        data = data.replaceAll("\r\n",";");
        try {
            File file = new File(path);
            File dir = new File(path.substring(0, path.lastIndexOf("/")));
            if (!dir.exists()) {
                dir.mkdirs();
            }
            FileWriter out = new FileWriter(file);
            out.write(data);
            out.flush();
            out.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
