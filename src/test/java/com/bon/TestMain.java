package com.bon;

import com.bon.common.util.GenerateUtil;
import com.bon.common.util.MD5Util;
import com.bon.common.util.MyLog;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

/**
 * @program: bon基础项目
 * @description: 基础测试
 * @author: Bon
 * @create: 2018-07-19 11:09
 **/
public class TestMain {
    private static final MyLog log = MyLog.getLog(generateApplication.class);

    @Before
    public void before() throws Exception {
        log.info(String.format("【测试开始】"));
    }

    @After
    public void after() throws Exception {
        log.info(String.format("【测试结束】"));
    }

    @Test
    public void utils() {
//        System.out.println(MD5Util.encode("123123",2));
        String url = TestMain.class.getResource("").toString();
        System.out.println(url);
    }
    @Test
    public void shiro() {
//        ByteSource byteSource = ByteSource.Util.bytes("bon");
//        HashedCredentialsMatcher obj = new HashedCredentialsMatcher("md5");
//        obj.setHashIterations(3);
//
//        System.out.println(byteSource);
//        System.out.println(ShiroUtil.encode("123123",3));
        SimpleHash hash = new SimpleHash("md5", "123123", "", 3);
        String encodedPassword = hash.toHex();
        System.out.println(encodedPassword);
        System.out.println(UUID.randomUUID().toString().replace("-",""));
    }
    @Test
    public void generate() throws Exception {
        GenerateUtil generateUtil = new GenerateUtil();
        List<String> tables = generateUtil.getTables();
        System.out.println(tables);
        generateUtil.createDTOClass("user","app");
        generateUtil.createVOClass("user","app");
        generateUtil.createListDTOClass("user","app");
        generateUtil.createServiceClass("user","app");
        generateUtil.createServiceImplClass("user","app");
    }
}
