package com.bon.common.util;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * @program: 后台-基础项目
 * @description: shiro工具类
 * @author: Bon
 * @create: 2018-08-01 15:43
 **/
public class ShiroUtil {

    private static final Integer count = 3;
    private static final String algorithm = "MD5";

    public static String md5encode(String plainText, String salt) {
        ByteSource byteSource = ByteSource.Util.bytes(salt);
        Object obj = new SimpleHash(algorithm, plainText, byteSource, count);
        return obj.toString();
    }
}
