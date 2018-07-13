package com.bon;

import com.bon.dao.GenerateMapper;
import com.bon.domain.dto.SysCreateTableDTO;
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

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    private static final MyLog LOG = MyLog.getLog(ApplicationTests.class);

    @Autowired
    private SysBaseService sysBaseService;

    @Autowired
    private GenerateMapper generateMapper;


    @Before
    public void before() throws Exception {
        LOG.info(String.format("【生成开始】"));
    }

    @After
    public void after() throws Exception {
        LOG.info(String.format("【生成结束】"));
    }

	@Test
	public void contextLoads() {
	}

    @Test
    public void generateClassByFile() throws Exception {
        SysCreateTableDTO dto = new SysCreateTableDTO();
        dto.setTableName("user_test");
        sysBaseService.generateClassByFile(dto);
    }


}
