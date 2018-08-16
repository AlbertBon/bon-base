package com.bon;

import com.bon.service.SysBaseService;
import com.bon.util.POIUtil;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: 后台-基础项目
 * @description: 生成数据库语句
 * @author: Bon
 * @create: 2018-08-16 16:48
 **/
public class generateMain {
    @Test
    public void generateViewSQL() throws Exception {
        List<String> tableList;
//        table = null;
        tableList = new ArrayList<>();
        tableList.add("role");
        tableList.add("user");
        String s = POIUtil.generateViewSql(new File(SysBaseService.class.getResource("/sql/baoli.xls").getFile()).getAbsolutePath(),tableList);
        System.out.println(s);
    }
}
