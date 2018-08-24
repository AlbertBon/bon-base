package com.bon.modules.sys.controller;

import com.bon.common.config.OperateInitConfig;
import com.bon.common.domain.vo.PageVO;
import com.bon.common.domain.vo.ResultBody;
import com.bon.common.util.StringUtils;
import com.bon.modules.sys.domain.dto.*;
import com.bon.modules.sys.domain.vo.*;
import com.bon.modules.sys.service.PermissionService;
import com.bon.modules.sys.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: bon-bon基础项目
 * @description: 权限管理模块
 * @author: Bon
 * @create: 2018-04-27 18:16
 **/
@Api(value = "权限管理模块",description = "权限管理模块")
@RequestMapping("/permission")
@RestController
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @ApiOperation(value = "获取所有权限(列表)",notes = "获取所有权限，返回结果是普通视图列表树形结构")
    @GetMapping(value = "/getAllPermission")
    @RequiresPermissions({"url:user:getAllPermission"})
    public ResultBody getAllPermission(){
        List<PermissionVO> list = permissionService.getAllPermission();
        return new ResultBody(list);
    }

    @ApiOperation(value = "获取所有权限(树形)",notes = "获取所有权限，返回结果是树形结构")
    @GetMapping(value = "/getAllPermissionTree")
    @RequiresPermissions({"url:user:getAllPermissionTree"})
    public ResultBody getAllPermissionTree(){
        List<PermissionTreeVO> list = permissionService.getAllPermissionTree();
        return new ResultBody(list);
    }

    @ApiOperation(value = "获取权限",notes = "根据权限类型和id查询相应信息")
    @PostMapping(value = "/getPermission")
    @RequiresPermissions({"url:user:getPermission"})
    public ResultBody getPermission(@RequestBody PermissionGetDTO dto){
        BaseVO vo = permissionService.getPermission(dto);
        return new ResultBody(vo.getMap());
    }

    @ApiOperation(value = "新增权限",notes = "")
    @PostMapping(value = "/savePermission")
    @RequiresPermissions({"url:user:savePermission"})
    public ResultBody savePermission(@RequestBody PermissionUpdateDTO dto){
        permissionService.savePermission(dto);
        return new ResultBody();
    }

    @ApiOperation(value = "修改权限",notes = "")
    @PostMapping(value = "/updatePermission")
    @RequiresPermissions({"url:user:updatePermission"})
    public ResultBody updatePermission(@RequestBody PermissionUpdateDTO dto){
        permissionService.updatePermission(dto);
        return new ResultBody();
    }

    @ApiOperation(value = "删除权限",notes = "")
    @GetMapping(value = "/deletePermission")
    @RequiresPermissions({"url:user:deletePermission"})
    public ResultBody deletePermission(@RequestParam Long key){
        permissionService.deletePermission(key);
        return new ResultBody();
    }

    @ApiOperation(value = "初始化",notes = "权限配置初始化，会根据controller生成对应权限，并自动分配给admin角色")
    @GetMapping(value = "/initPermission")
    @RequiresRoles({"admin"})
    public ResultBody initPermission() throws Exception {
        OperateInitConfig config = new OperateInitConfig();
        config.init();
        return new ResultBody();
    }

    @ApiOperation(value = "生成Vue",notes = "")
    @GetMapping(value = "/generateVueMenu")
    @RequiresRoles({"admin"})
    public ResultBody generateVueMenu(@RequestParam String tableName,@RequestParam String modules) throws Exception {
        permissionService.generateMenuToVue(StringUtils.camel2Underline(tableName),modules);
        return new ResultBody();
    }
}
