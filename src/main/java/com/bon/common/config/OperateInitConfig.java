package com.bon.common.config;

import com.bon.common.domain.dto.BaseDTO;
import com.bon.common.util.MyLog;
import com.bon.common.util.SpringUtil;
import com.bon.common.util.StringUtils;
import com.bon.modules.sys.dao.SysPermissionMapper;
import com.bon.modules.sys.dao.SysUserExtendMapper;
import com.bon.modules.sys.domain.dto.PermissionUpdateDTO;
import com.bon.modules.sys.domain.entity.SysPermission;
import com.bon.modules.sys.domain.entity.SysUrl;
import com.bon.modules.sys.domain.enums.PermissionType;
import com.bon.modules.sys.service.PermissionService;
import com.bon.modules.sys.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
@Component
@Order(value = 1)
@Transactional
public class OperateInitConfig implements CommandLineRunner{
    private static final MyLog log = MyLog.getLog(OperateInitConfig.class);

    public void init() throws ClassNotFoundException {
        PermissionService permissionService = SpringUtil.getBean(PermissionService.class);
        SysUserExtendMapper userExtendMapper = SpringUtil.getBean(SysUserExtendMapper.class);
        SysPermissionMapper permissionMapper = SpringUtil.getBean(SysPermissionMapper.class);

        List<String> permissionFlagList = userExtendMapper.getPermissionFlag();

        //获取控制层
        Map<String, Object> controllers = SpringUtil.getApplicationContext().getBeansWithAnnotation(RestController.class);

        for(Object controller :controllers.values()){
            String className = controller.getClass().getName();
            String[] s = className.split("\\$\\$");

            Class<?> clazz = Class.forName(s[0]);

            String parentRemark = "";
            String parentName = "";
            Long parentId = 0L;
            String parentPath = "";
            Api api = clazz.getAnnotation(Api.class);
            if(api!=null){
                parentName = api.value();
                parentRemark = api.description();
            }else {
                continue;
            }
            RequestMapping parentRequestMapping = clazz.getAnnotation(RequestMapping.class);
            if(parentRequestMapping != null){
                parentPath = parentRequestMapping.value()[0];
            }
            //根据权限标识查询出父权限
            BaseDTO baseDTO = new BaseDTO();
            baseDTO.andFind(new SysPermission(),"permissionFlag",parentName);
            SysPermission parentPermission = permissionMapper.selectOneByExample(baseDTO.getExample());
            for (Method method : clazz.getMethods()) {
                String remark = "";
                String name = "";
                String path = "";
                String permissionFlag = "";
                //先判断是否有权限配置，没有则跳至下一个method
                RequiresPermissions requiresPermissions = method.getAnnotation(RequiresPermissions.class);
                if (requiresPermissions != null) {
                    permissionFlag = requiresPermissions.value()[0];
                }
                if(StringUtils.isBlank(permissionFlag)){
                    continue;
                }
                if(permissionFlagList.contains(permissionFlag)){
                    log.info("权限名{}已存在,不添加",permissionFlag);
                    continue;
                }

                //生成url地址
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
                //加上父路径
                path = parentPath + path;

                ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
                if(apiOperation!=null){
                    name = apiOperation.value();
                    remark = apiOperation.notes();
                }

                //如果父id为0以及根据标识查出的父权限为空时，则需要先增加父id
                if(0 == parentId && null == parentPermission ){
                    PermissionUpdateDTO parentDTO = new PermissionUpdateDTO();
                    parentDTO.setType(PermissionType.URL.getKey());
                    parentDTO.setPermissionFlag(parentName);
                    SysUrl parentSysUrl = new SysUrl();
                    parentSysUrl.setUrlName(parentName);
                    parentSysUrl.setUrlPath(parentPath);
                    parentSysUrl.setUrlRemark(parentRemark);
                    parentDTO.setUrl(parentSysUrl);
                    parentId = permissionService.savePermission(parentDTO);
                    parentPermission = permissionMapper.selectByPrimaryKey(parentId);

                    PermissionUpdateDTO dto = new PermissionUpdateDTO();
                    dto.setType(PermissionType.URL.getKey());
                    dto.setPermissionFlag(permissionFlag);
                    SysUrl sysUrl = new SysUrl();
                    sysUrl.setUrlName(name);
                    sysUrl.setUrlPath(path);
                    sysUrl.setUrlRemark(remark);
                    dto.setUrl(sysUrl);
                    dto.setParentId(parentId);
                    permissionService.savePermission(dto);
                }else {
                    PermissionUpdateDTO dto = new PermissionUpdateDTO();
                    dto.setType(PermissionType.URL.getKey());
                    dto.setPermissionFlag(permissionFlag);
                    SysUrl sysUrl = new SysUrl();
                    sysUrl.setUrlName(name);
                    sysUrl.setUrlPath(path);
                    sysUrl.setUrlRemark(remark);
                    dto.setUrl(sysUrl);
                    if(parentPermission != null){
                        dto.setParentId(parentPermission.getPermissionId());
                    }else {
                        dto.setParentId(parentId);
                    }

                    permissionService.savePermission(dto);
                }


                log.info("权限{}写入数据库，接口url为{}",permissionFlag,path);

            }
        }
    }

    @Override
    public void run(String... strings) throws Exception {
        this.init();
    }
}
