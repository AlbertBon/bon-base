package com.bon;

import com.bon.dao.SysBaseMapper;
import com.bon.dao.UserExtendMapper;
import com.bon.domain.dto.SysGenerateClassDTO;
import com.bon.domain.entity.SysBase;
import com.bon.service.SysBaseService;
import com.bon.util.MyLog;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
    private UserExtendMapper userExtendMapper;


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
        dto.setTableName("menu");
        dtoList.add(dto);
        sysBaseService.generateClass(dtoList);
    }

    @Test
    /**
     * 生成所有数据表类和mapper
     */
    public void generateAllClass() throws Exception {
        List<SysBase> sysBaseList = SysBaseMapper.listTables();
        List<SysGenerateClassDTO> dtoList = new ArrayList<>();
        for(SysBase sysBase:sysBaseList){
            if("sys_base".equals(sysBase.getTableName())){
                continue;
            }
            SysGenerateClassDTO dto = new SysGenerateClassDTO();
            dto.setTableName(sysBase.getTableName());
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
