package com.bon;

import com.bon.common.util.GenerateCoreUtil;
import com.bon.modules.sys.dao.SysBaseExtendMapper;
import com.bon.modules.sys.dao.SysBaseMapper;
import com.bon.modules.sys.dao.SysUserExtendMapper;
import com.bon.modules.sys.domain.dto.SysGenerateClassDTO;
import com.bon.modules.sys.domain.entity.SysBase;
import com.bon.modules.sys.service.PermissionService;
import com.bon.modules.sys.service.SysBaseService;
import com.bon.common.util.MyLog;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sun.nio.cs.ext.MS874;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Bon
 * @Description: 生成类信息
 * @param null
 * @return:
 * @Date: 2018/8/16 16:50
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class generateApplication {

    private static final MyLog LOG = MyLog.getLog(generateApplication.class);

    @Autowired
    private SysBaseService sysBaseService;

    @Autowired
    private SysBaseMapper SysBaseMapper;

    @Autowired
    private SysBaseExtendMapper sysBaseExtendMapper;

    @Autowired
    private SysUserExtendMapper sysUserExtendMapper;

    @Autowired
    private PermissionService permissionService;


    @Before
    public void before() throws Exception {
        LOG.info(String.format("【生成开始】"));
    }

    @After
    public void after() throws Exception {
        LOG.info(String.format("【生成结束】"));
    }

    /**
     * 根据表名生成所有类和视图，并将视图的菜单生成(需要生成多个表的文件时加入mapList中即可)
     * @throws Exception
     */
    @Test
    public void generateAllAndVUE() throws Exception {
//        List<Map<String,String>> maps = new ArrayList<>();
//        Map<String,String> map = new HashMap<>();
//        map.put("tableName","test");
//        map.put("modules","app");
//        maps.add(map);
//        for(Map<String,String> map1 : maps){
//            GenerateCoreUtil.generateAll(map1.get("tableName"),map1.get("modules"));
//            GenerateCoreUtil.generateVUE(map1.get("tableName"),map1.get("modules"));
//            permissionService.generateMenuToVue(map1.get("tableName"),map1.get("modules"));
//        }
    }

    /**
     * 根据系统基础表生成所有文件
     */
    @Test
    public void generateAllClass() throws Exception {
        List<SysBase> sysBaseList = sysBaseExtendMapper.listTables();
        for (SysBase sysBase : sysBaseList) {
            GenerateCoreUtil.generateAll(sysBase.getTableName(), sysBase.getModules());
        }
    }

    /**
     * 根据excel文件生成数据库
     */
    @Test
    public void generateTable() {
        sysBaseService.generateTable(new File(SysBaseService.class.getResource("/sql/generate.xls").getFile()),null);
    }

    /**
     * 生成某几个数据表类和mapper(需要用到mybatis生成工具)
     */
//    @Test
//    public void generateClass() throws Exception {
//        List<SysGenerateClassDTO> dtoList = new ArrayList<>();
//        SysGenerateClassDTO dto = new SysGenerateClassDTO();
//        List<String> tableNameList = new ArrayList<>();
//        //表名
//        /*
//        tableNameList.add("sys_user");//基础表
//        tableNameList.add("sys_user_role");//基础表
//        tableNameList.add("sys_role");//基础表
//        tableNameList.add("sys_role_permission");//基础表
//        tableNameList.add("sys_permission");//基础表
//        tableNameList.add("sys_menu");//基础表
//        tableNameList.add("sys_base");//基础表
//        */
//        tableNameList.add("test");
//        dto.setTableNameList(tableNameList);
//        //模块
//        dto.setModules("app");
//        //是否生成扩展类（service、controller、dto、vo等信息）
//        if("sys".equals(dto.getModules())){//固定系统模块不生成扩展类
//            dto.setIsExtend((byte) 0);
//        }else {
//            //dto.setIsExtend((byte) 0);//否
//            dto.setIsExtend((byte) 1);//是
//        }
//        dtoList.add(dto);
//        sysBaseService.generateClass(dtoList);
//    }
    /**
     * 根据系统基础表生成所有数据表类和mapper(需要用到mybatis生成工具)
     */
//    @Test
//    public void generateAllClass() throws Exception {
//        List<SysBase> sysBaseList = sysBaseExtendMapper.listTables();
//        List<SysBase> sysBaseList = sysBaseExtendMapper.listTables();
//        List<SysGenerateClassDTO> dtoList = new ArrayList<>();
//        Map<String,List<String>> modulesMap = new HashMap<>();
//        //遍历生成按照modules分组的map
//        for(SysBase sysBase:sysBaseList){
//            if("sys_base".equals(sysBase.getTableName())){
//                continue;
//            }
//            List<String> tableNameList = modulesMap.get(sysBase.getModules());
//            if(null!=tableNameList){
//                tableNameList.add(sysBase.getTableName());
//            }else {
//                tableNameList = new ArrayList<>();
//                tableNameList.add(sysBase.getTableName());
//                modulesMap.put(sysBase.getModules(),tableNameList);
//            }
//        }
//        //循环取出map放入list
//        for(Map.Entry<String,List<String>> entry:modulesMap.entrySet()){
//            SysGenerateClassDTO dto = new SysGenerateClassDTO();
//            dto.setModules(entry.getKey());
//            dto.setTableNameList(entry.getValue());
//            dto.setIsExtend((byte) 0);
//            dtoList.add(dto);
//        }
//        sysBaseService.generateClass(dtoList);
//    }


}
