package com.bon;

import com.bon.common.config.YamlConfig;
import com.bon.modules.sys.dao.SysBaseMapper;
import com.bon.modules.sys.dao.SysUserExtendMapper;
import com.bon.modules.sys.service.SysBaseService;
import com.bon.common.util.MyLog;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestApplication {

    private static final MyLog LOG = MyLog.getLog(TestApplication.class);

    @Autowired
    private SysBaseService sysBaseService;

    @Autowired
    private SysBaseMapper SysBaseMapper;

    @Autowired
    private SysUserExtendMapper sysUserExtendMapper;

    @Autowired
    private YamlConfig config;


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
//        List<Role> roles = userExtendMapper.getRoleByUsername("bon");
//        for(Role role:roles){
//            System.out.println(role);
//        }
//        SysUser user = .(1L);
//        System.out.println(user.getUserId());
        //测试加载yml文件
        System.out.println("simpleProp: " + config.getMapProps().get("corsHost"));
	}

}
