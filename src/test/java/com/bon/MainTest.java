package com.bon;

import com.bon.util.MD5Util;
import com.bon.util.MyLog;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
        log.info(String.format("【生成开始】"));
    }

    @After
    public void after() throws Exception {
        log.info(String.format("【生成结束】"));
    }

    @Test
    public void utils() {
        System.out.println(MD5Util.encode("123123",2));
    }
}
