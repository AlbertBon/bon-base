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
 * @description: 用户管理模块
 * @author: Bon
 * @create: 2018-04-27 18:16
 **/
@Api(value = "user",description = "用户管理模块")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "获取用户",notes = "根据id获取用户信息")
    @GetMapping(value = "/user/getUser")
    @RequiresPermissions({"url:user:getUser"})
    public ResultBody getUser(@RequestParam Long key){
        UserVO vo= userService.getUser(key);
        return new ResultBody(vo);
    }

    @ApiOperation(value = "新增用户")
    @PostMapping(value = "/user/saveUser",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions({"url:user:saveUser"})
    public ResultBody saveUser(@RequestBody UserDTO user){
        userService.saveUser(user);
        return new ResultBody();
    }

    @ApiOperation(value = "根据条件获取用户列表")
    @PostMapping(value = "/user/listUser",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions({"url:user:listUser"})
    public ResultBody listUser(@RequestBody UserListDTO listDTO){
        PageVO pageVO = userService.listUser(listDTO);
        return new ResultBody(pageVO);
    }

    @ApiOperation(value = "修改用户")
    @PostMapping(value = "/user/updateUser",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions({"url:user:updateUser"})
    public ResultBody updateUser(@RequestBody UserDTO dto){
        userService.updateUser(dto);
        return new ResultBody();
    }

    @ApiOperation(value = "删除用户")
    @GetMapping(value = "/user/deleteUser")
    @RequiresPermissions({"url:user:deleteUser"})
    public ResultBody deleteUser(@RequestParam Long key){
        userService.deleteUser(key);
        return new ResultBody();
    }

    @ApiOperation(value = "获取所有用户")
    @GetMapping(value = "/role/getAllUser")
    @RequiresPermissions({"url:user:getAllUser"})
    public ResultBody getAllUser(){
        List<UserVO> list = userService.getAllUser();
        return new ResultBody(list);
    }

    @ApiOperation(value = "获取角色")
    @GetMapping(value = "/role/getRole")
    @RequiresPermissions({"url:user:getRole"})
    public ResultBody getRole(@RequestParam Long key){
        RoleVO vo= userService.getRole(key);
        return new ResultBody(vo);
    }

    @ApiOperation(value = "新增角色")
    @PostMapping(value = "/role/saveRole",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions({"url:user:saveRole"})
    public ResultBody saveRole(@RequestBody RoleDTO dto){
        userService.saveRole(dto);
        return new ResultBody();
    }

    @ApiOperation(value = "根据条件获取角色列表")
    @PostMapping(value = "/role/listRole",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions({"url:user:listRole"})
    public ResultBody listRole(@RequestBody RoleListDTO dto){
        PageVO pageVO = userService.listRole(dto);
        return new ResultBody(pageVO);
    }

    @ApiOperation(value = "修改角色")
    @ApiResponse(code = 200, message = "success")
    @PostMapping(value = "/role/updateRole",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions({"url:user:updateRole"})
    public ResultBody updateRole(@RequestBody RoleDTO dto){
        userService.updateRole(dto);
        return new ResultBody();
    }

    @ApiOperation(value = "删除角色")
    @GetMapping(value = "/role/deleteRole")
    @RequiresPermissions({"url:user:deleteRole"})
    public ResultBody deleteRole(@RequestParam Long key){
        userService.deleteRole(key);
        return new ResultBody();
    }

    @ApiOperation(value = "获取所有角色")
    @GetMapping(value = "/role/getAllRole")
    @RequiresPermissions({"url:user:getAllRole"})
    public ResultBody getAllRole(){
        List<RoleVO> list = userService.getAllRole();
        return new ResultBody(list);
    }

    @ApiOperation(value = "获取所有权限")
    @GetMapping(value = "/permission/getAllPermission")
    @RequiresPermissions({"url:user:getAllPermission"})
    public ResultBody getAllPermission(){
        List<PermissionVO> list = userService.getAllPermission();
        return new ResultBody(list);
    }

    @ApiOperation(value = "获取所有权限树形结构")
    @GetMapping(value = "/permission/getAllPermissionTree")
    @RequiresPermissions({"url:user:getAllPermissionTree"})
    public ResultBody getAllPermissionTree(){
        List<PermissionTreeVO> list = userService.getAllPermissionTree();
        return new ResultBody(list);
    }

    @ApiOperation(value = "根据权限类型和id查询相应信息")
    @PostMapping(value = "/permission/getPermission")
    @RequiresPermissions({"url:user:getPermission"})
    public ResultBody getPermission(@RequestBody PermissionGetDTO dto){
        BaseVO vo = userService.getPermission(dto);
        return new ResultBody(vo.getMap());
    }

    @ApiOperation(value = "新增权限")
    @PostMapping(value = "/permission/savePermission")
    @RequiresPermissions({"url:user:savePermission"})
    public ResultBody savePermission(@RequestBody PermissionUpdateDTO dto){
        userService.savePermission(dto);
        return new ResultBody();
    }

    @ApiOperation(value = "修改权限")
    @PostMapping(value = "/permission/updatePermission")
    @RequiresPermissions({"url:user:updatePermission"})
    public ResultBody updatePermission(@RequestBody PermissionUpdateDTO dto){
        userService.updatePermission(dto);
        return new ResultBody();
    }

    @ApiOperation(value = "修改权限")
    @GetMapping(value = "/permission/deletePermission")
    @RequiresPermissions({"url:user:deletePermission"})
    public ResultBody deletePermission(@RequestParam Long key){
        userService.deletePermission(key);
        return new ResultBody();
    }

    @ApiOperation(value = "权限配置初始化")
    @GetMapping(value = "/permission/initPermission")
//    @RequiresPermissions({"url:user:initPermission"})
    public ResultBody initPermission(@RequestParam Long key) throws Exception {
        OperateInitConfig config = new OperateInitConfig();
        config.init();
        return new ResultBody();
    }
}
