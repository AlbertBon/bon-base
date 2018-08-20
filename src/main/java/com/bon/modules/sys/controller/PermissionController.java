package com.bon.modules.sys.controller;

import com.bon.common.config.OperateInitConfig;
import com.bon.common.domain.vo.PageVO;
import com.bon.common.domain.vo.ResultBody;
import com.bon.modules.sys.domain.dto.*;
import com.bon.modules.sys.domain.vo.*;
import com.bon.modules.sys.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
    private UserService userService;

    @ApiOperation(value = "获取所有权限(列表)",notes = "获取所有权限，返回结果是普通视图列表树形结构")
    @GetMapping(value = "/getAllPermission")
    @RequiresPermissions({"url:user:getAllPermission"})
    public ResultBody getAllPermission(){
        List<PermissionVO> list = userService.getAllPermission();
        return new ResultBody(list);
    }

    @ApiOperation(value = "获取所有权限(树形)",notes = "获取所有权限，返回结果是树形结构")
    @GetMapping(value = "/getAllPermissionTree")
    @RequiresPermissions({"url:user:getAllPermissionTree"})
    public ResultBody getAllPermissionTree(){
        List<PermissionTreeVO> list = userService.getAllPermissionTree();
        return new ResultBody(list);
    }

    @ApiOperation(value = "获取权限",notes = "根据权限类型和id查询相应信息")
    @PostMapping(value = "/getPermission")
    @RequiresPermissions({"url:user:getPermission"})
    public ResultBody getPermission(@RequestBody PermissionGetDTO dto){
        BaseVO vo = userService.getPermission(dto);
        return new ResultBody(vo.getMap());
    }

    @ApiOperation(value = "新增权限",notes = "")
    @PostMapping(value = "/savePermission")
    @RequiresPermissions({"url:user:savePermission"})
    public ResultBody savePermission(@RequestBody PermissionUpdateDTO dto){
        userService.savePermission(dto);
        return new ResultBody();
    }

    @ApiOperation(value = "修改权限",notes = "")
    @PostMapping(value = "/updatePermission")
    @RequiresPermissions({"url:user:updatePermission"})
    public ResultBody updatePermission(@RequestBody PermissionUpdateDTO dto){
        userService.updatePermission(dto);
        return new ResultBody();
    }

    @ApiOperation(value = "修改权限",notes = "")
    @GetMapping(value = "/deletePermission")
    @RequiresPermissions({"url:user:deletePermission"})
    public ResultBody deletePermission(@RequestParam Long key){
        userService.deletePermission(key);
        return new ResultBody();
    }

    @ApiOperation(value = "初始化",notes = "权限配置初始化，会根据controller生成对应权限，并自动分配给admin角色")
    @GetMapping(value = "/initPermission")
//    @RequiresPermissions({"url:user:initPermission"})
    public ResultBody initPermission(@RequestParam Long key) throws Exception {
        OperateInitConfig config = new OperateInitConfig();
        config.init();
        return new ResultBody();
    }
}
