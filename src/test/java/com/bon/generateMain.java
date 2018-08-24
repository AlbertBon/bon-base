package com.bon;

import com.bon.common.util.GenerateCoreUtil;
import com.bon.modules.sys.service.SysBaseService;
import com.bon.common.util.POIUtil;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: 后台-基础项目
 * @description: main方法生成
 * @author: Bon
 * @create: 2018-08-16 16:48
 **/
public class generateMain {

    /**
     * 生成所有文件(需要生成多个表的文件时加入mapList中即可)
     */
    @Test
    public void generateAll() throws Exception {
        List<Map<String,String>> maps = new ArrayList<>();
        Map<String,String> map;

//        map = new HashMap<>();
//        map.put("tableName","sys_permission");
//        map.put("modules","sys");
//        maps.add(map);
//
//        map = new HashMap<>();
//        map.put("tableName","sys_menu");
//        map.put("modules","sys");
//        maps.add(map);
//
        for(Map<String,String> map1 : maps) {
            GenerateCoreUtil.generateAll(map1.get("tableName"),map1.get("modules"));
        }
    }
    /**
     * 生成vue文件(需要生成多个表的文件时加入mapList中即可)
     */
    @Test
    public void generateVUE() throws Exception {
        List<Map<String,String>> maps = new ArrayList<>();
        Map<String,String> map;

//        map = new HashMap<>();
//        map.put("tableName","sys_permission");
//        map.put("modules","sys");
//        maps.add(map);
//
//        map = new HashMap<>();
//        map.put("tableName","sys_menu");
//        map.put("modules","sys");
//        maps.add(map);

        for(Map<String,String> map1 : maps) {
            GenerateCoreUtil.generateVUE(map1.get("tableName"),map1.get("modules"));
        }
    }

    /**
     *只生成entity文件，用于(需要生成多个表的文件时加入mapList中即可)
     */
    @Test
    public void generateEntity() throws Exception {
        List<Map<String,String>> maps = new ArrayList<>();
        Map<String,String> map;

//        map = new HashMap<>();
//        map.put("tableName","sys_permission");
//        map.put("modules","sys");
//        maps.add(map);
//
//        map = new HashMap<>();
//        map.put("tableName","sys_menu");
//        map.put("modules","sys");
//        maps.add(map);

        for(Map<String,String> map1 : maps) {
            GenerateCoreUtil.createEntityClass(map1.get("tableName"),map1.get("modules"));
        }
    }
    /**
     * @Author: Bon
     * @Description: 根据excel生成数据库语句
     * @param
     * @return: void
     * @Date: 2018/8/19 12:57
     */
    @Test
    public void generateViewSQL() throws Exception {
        List<String> tableList;
        tableList = new ArrayList<>();
        tableList.add("role");
        tableList.add("user");
        String s = POIUtil.generateViewSql(new File(SysBaseService.class.getResource("/sql/baoli.xls").getFile()).getAbsolutePath(),tableList);
        System.out.println(s);
    }


    /**
     * @Author: Bon
     * @Description: 生成扩展文件（可按要求生成某个扩展文件）
     * @param
     * @return: void
     * @Date: 2018/8/19 12:58
     */
    @Test
    public void generateExtend() throws Exception {
        GenerateCoreUtil generateUtil = new GenerateCoreUtil();
        String tableName = "test";
        String modules = "app";
        //实体类文件
        generateUtil.createEntityClass(tableName,modules);
        //参数类文件
        generateUtil.createDTOClass(tableName,modules);
        //视图文件
        generateUtil.createVOClass(tableName,modules);
        //列表参数类文件
        generateUtil.createListDTOClass(tableName,modules);
        //服务接口文件
        generateUtil.createServiceClass(tableName,modules);
        //服务实现文件
        generateUtil.createServiceImplClass(tableName,modules);
        //mapper文件
        generateUtil.createMapperClass(tableName,modules);
        //mapper xml文件
        generateUtil.createMapperXML(tableName,modules);
        //控制层文件
        generateUtil.createControllerClass(tableName,modules);
        //vue文件
        generateUtil.generateVUE(tableName,modules);
    }
}
