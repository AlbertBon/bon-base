package com.bon.common.config;

import com.bon.common.util.MyLog;
import com.bon.common.util.SpringUtil;
import com.bon.common.util.StringUtils;
import com.bon.modules.sys.dao.SysUserExtendMapper;
import com.bon.modules.sys.domain.dto.PermissionUpdateDTO;
import com.bon.modules.sys.domain.entity.SysUrl;
import com.bon.modules.sys.domain.enums.PermissionType;
import com.bon.modules.sys.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @program: 后台-基础项目
 * @description: 系统权限配置初始化
 * @author: Bon
 * @create: 2018-08-20 17:50
 **/
public class OperateInitConfig {
    private static MyLog log = MyLog.getLog(OperateInitConfig.class);

    public void init() throws ClassNotFoundException {
        UserService userService = SpringUtil.getBean(UserService.class);
        SysUserExtendMapper userExtendMapper = SpringUtil.getBean(SysUserExtendMapper.class);

        List<String> permissionFlagList = userExtendMapper.getPermissionFlag();

        //获取控制层
        Map<String, Object> controllers = SpringUtil.getApplicationContext().getBeansWithAnnotation(RestController.class);

        for(Object controller :controllers.values()){
            String className = controller.getClass().getName();
            String[] s = className.split("\\$\\$");

            Class<?> clazz = Class.forName(s[0]);

            String remarkParent = "";
            Long parentId = 0L;
            Api api = clazz.getAnnotation(Api.class);
            if(api!=null){
                remarkParent = api.value();
            }
            for (Method method : clazz.getMethods()) {
                String remark = "";
                String path = "";
                String permissionFlag = "";

                RequiresPermissions requiresPermissions = method.getAnnotation(RequiresPermissions.class);
                if (requiresPermissions != null) {
                    permissionFlag = requiresPermissions.value()[0];
                }

                if(StringUtils.isBlank(permissionFlag)){
                    continue;
                }

                if(permissionFlagList.contains(remark)){
                    log.info("权限名{}重复",permissionFlag);
                    continue;
                }

                GetMapping getMapping = method.getAnnotation(GetMapping.class);
                PostMapping postMapping = method.getAnnotation(PostMapping.class);
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                if(getMapping!=null){
                    path = getMapping.value()[0];
                }else if(postMapping!=null){
                    path = postMapping.value()[0];
                }else if(requestMapping!=null){
                    path = requestMapping.value()[0];
                }

                ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
                if(apiOperation!=null){
                    remark = apiOperation.value();
                }

                if(0 == parentId){
                    PermissionUpdateDTO dto = new PermissionUpdateDTO();
                    dto.setType(PermissionType.URL.getKey());
                    dto.setPermissionFlag(permissionFlag);
                    SysUrl sysUrl = new SysUrl();
                    sysUrl.setUrlName(remark);
                    sysUrl.setUrlPath(path);
                    sysUrl.setUrlRemark(remark);
                    dto.setUrl(sysUrl);
                    userService.savePermission(dto);
                }

                PermissionUpdateDTO dto = new PermissionUpdateDTO();
                dto.setType(PermissionType.URL.getKey());
                dto.setPermissionFlag(permissionFlag);
                SysUrl sysUrl = new SysUrl();
                sysUrl.setUrlName(remark);
                sysUrl.setUrlPath(path);
                sysUrl.setUrlRemark(remark);
                dto.setUrl(sysUrl);
                userService.savePermission(dto);
                log.info("权限{}写入数据库，接口url为{}，接口名为{}",permissionFlag,path,remark);

            }
        }
    }

}
