package com.bon;

import com.bon.modules.sys.dao.SysBaseExtendMapper;
import com.bon.modules.sys.dao.SysBaseMapper;
import com.bon.modules.sys.dao.SysUserExtendMapper;
import com.bon.modules.sys.domain.dto.SysGenerateClassDTO;
import com.bon.modules.sys.domain.entity.SysBase;
import com.bon.modules.sys.service.SysBaseService;
import com.bon.common.util.MyLog;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
    SysBaseExtendMapper sysBaseExtendMapper;

    @Autowired
    private SysUserExtendMapper sysUserExtendMapper;


    @Before
    public void before() throws Exception {
        LOG.info(String.format("【生成开始】"));
    }

    @After
    public void after() throws Exception {
        LOG.info(String.format("【生成结束】"));
    }

    @Test
    /**
     * 生成某几个数据表类和mapper
     */
    public void generateClass() throws Exception {
        List<SysGenerateClassDTO> dtoList = new ArrayList<>();
        SysGenerateClassDTO dto = new SysGenerateClassDTO();
        List<String> tableNameList = new ArrayList<>();
        //表名
        tableNameList.add("test");
        dto.setTableNameList(tableNameList);
        //模块
        dto.setModules("app");
        //是否生成扩展类
        dto.setIsExtend((byte) 0);
        dtoList.add(dto);
        sysBaseService.generateClass(dtoList);
    }

    @Test
    /**
     * 生成所有数据表类和mapper
     */
    public void generateAllClass() throws Exception {
        List<SysBase> sysBaseList = sysBaseExtendMapper.listTables();
        List<SysGenerateClassDTO> dtoList = new ArrayList<>();
        Map<String,List<String>> modulesMap = new HashMap<>();
        //遍历生成按照modules分组的map
        for(SysBase sysBase:sysBaseList){
            if("sys_base".equals(sysBase.getTableName())){
                continue;
            }
            List<String> tableNameList = modulesMap.get(sysBase.getModules());
            if(null!=tableNameList){
                tableNameList.add(sysBase.getTableName());
            }else {
                tableNameList = new ArrayList<>();
                tableNameList.add(sysBase.getTableName());
                modulesMap.put(sysBase.getModules(),tableNameList);
            }
        }
        //循环取出map放入list
        for(Map.Entry<String,List<String>> entry:modulesMap.entrySet()){
            SysGenerateClassDTO dto = new SysGenerateClassDTO();
            dto.setModules(entry.getKey());
            dto.setTableNameList(entry.getValue());
            dto.setIsExtend((byte) 0);
            dtoList.add(dto);
        }
        sysBaseService.generateClass(dtoList);
    }

    @Test
    /**
     * 生成数据库
     */
    public void generateTable() {
        sysBaseService.generateTable(new File(SysBaseService.class.getResource("/sql/generate.xls").getFile()));
    }

}
