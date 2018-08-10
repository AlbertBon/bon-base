package com.bon;

import com.bon.dao.GenerateMapper;
import com.bon.dao.SysBaseMapper;
import com.bon.dao.UserExtendMapper;
import com.bon.domain.dto.SysGenerateClassDTO;
import com.bon.domain.entity.Role;
import com.bon.domain.entity.SysBase;
import com.bon.service.SysBaseService;
import com.bon.util.MyLog;
import com.bon.util.POIUtil;
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
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    private static final MyLog LOG = MyLog.getLog(ApplicationTests.class);

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
	public void test() {
        List<Role> roles = userExtendMapper.getRoleByUsername("bon");
        for(Role role:roles){
            System.out.println(role);
        }
	}

    @Test
    /**
     * 生成某几个数据表类和mapper
     */
    public void generateClass() throws Exception {
        List<SysGenerateClassDTO> dtoList = new ArrayList<>();
        SysGenerateClassDTO dto = new SysGenerateClassDTO();
        dto.setTableName("user");
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
    public void generateTable() {
        sysBaseService.generateTable(new File(SysBaseService.class.getResource("/sql/generate.xls").getFile()));
    }

    @Test
    public void generateTableSQL() throws Exception {
        String s=sysBaseService.generateTableSQL(new File(SysBaseService.class.getResource("/sql/generate.xls").getFile()));
        System.out.println(s);
    }


}
