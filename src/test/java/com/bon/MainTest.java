package com.bon;

import com.bon.util.MD5Util;
import com.bon.util.MyLog;
import com.bon.util.ShiroUtil;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.sound.midi.Soundbank;
import java.util.UUID;

/**
 * @program: bon基础项目
 * @description: 基础测试
 * @author: Bon
 * @create: 2018-07-19 11:09
 **/
public class MainTest {
    private static final MyLog log = MyLog.getLog(ApplicationTests.class);

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
        System.out.println(MD5Util.encode("123123",2));
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
}
