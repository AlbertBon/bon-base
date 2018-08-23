package com.bon;

import com.bon.common.util.GenerateCoreUtil;
import com.bon.common.util.MyLog;
import com.bon.common.util.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
//        String url = TestMain.class.getResource("").toString();
//        System.out.println(url);

//        String s = "tablName";
//        System.out.println(StringUtils.underline2Camel(s,false));
//        System.out.println(StringUtils.upperCase(s));

//        Long paidAmount = 805812L;
//        Long poundage = paidAmount*2/1000;
//        if(poundage<=1000){
//            paidAmount += 1000;
//        }else{
//            paidAmount += poundage;
//        }
//        System.out.println(poundage);
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
