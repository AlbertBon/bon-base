package com.bon.controller;

import com.bon.common.vo.PageVO;
import com.bon.common.vo.ResultBody;
import com.bon.domain.dto.*;
import com.bon.domain.vo.*;
import com.bon.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
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
@Api("用户管理模块")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "获取用户")
    @GetMapping(value = "/user/getUser")
    public ResultBody getUser(@RequestParam Long key){
        UserVO vo= userService.getUser(key);
        return new ResultBody(vo);
    }

    @ApiOperation(value = "新增用户")
    @ApiResponse(code = 200, message = "success")
    @PostMapping(value = "/user/saveUser",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBody saveUser(@RequestBody UserDTO user){
        userService.saveUser(user);
        return new ResultBody();
    }

    @ApiOperation(value = "根据条件获取用户列表")
    @ApiResponse(code = 200, message = "success" )
    @PostMapping(value = "/user/listUser",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBody listUser(@RequestBody UserListDTO listDTO){
        PageVO pageVO = userService.listUser(listDTO);
        return new ResultBody(pageVO);
    }

    @ApiOperation(value = "修改用户")
    @ApiResponse(code = 200, message = "success")
    @PostMapping(value = "/user/updateUser",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBody updateUser(@RequestBody UserDTO dto){
        userService.updateUser(dto);
        return new ResultBody();
    }

    @ApiOperation(value = "删除用户")
    @GetMapping(value = "/user/deleteUser")
    public ResultBody deleteUser(@RequestParam Long key){
        userService.deleteUser(key);
        return new ResultBody();
    }

    @ApiOperation(value = "获取所有用户")
    @GetMapping(value = "/role/getAllUser")
    public ResultBody getAllUser(){
        List<UserVO> list = userService.getAllUser();
        return new ResultBody(list);
    }

    @ApiOperation(value = "获取角色")
    @GetMapping(value = "/role/getRole")
    public ResultBody getRole(@RequestParam Long key){
        RoleVO vo= userService.getRole(key);
        return new ResultBody(vo);
    }

    @ApiOperation(value = "新增角色")
    @PostMapping(value = "/role/saveRole",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBody saveRole(@RequestBody RoleDTO dto){
        userService.saveRole(dto);
        return new ResultBody();
    }

    @ApiOperation(value = "根据条件获取角色列表")
    @PostMapping(value = "/role/listRole",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBody listRole(@RequestBody RoleListDTO dto){
        PageVO pageVO = userService.listRole(dto);
        return new ResultBody(pageVO);
    }

    @ApiOperation(value = "修改角色")
    @ApiResponse(code = 200, message = "success")
    @PostMapping(value = "/role/updateRole",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBody updateRole(@RequestBody RoleDTO dto){
        userService.updateRole(dto);
        return new ResultBody();
    }

    @ApiOperation(value = "删除角色")
    @GetMapping(value = "/role/deleteRole")
    public ResultBody deleteRole(@RequestParam Long key){
        userService.deleteRole(key);
        return new ResultBody();
    }

    @ApiOperation(value = "获取所有角色")
    @GetMapping(value = "/role/getAllRole")
    public ResultBody getAllRole(){
        List<RoleVO> list = userService.getAllRole();
        return new ResultBody(list);
    }

    @ApiOperation(value = "获取菜单")
    @GetMapping(value = "/menu/getMenu")
    public ResultBody getMenu(@RequestParam Long key){
        MenuVO vo= userService.getMenu(key);
        return new ResultBody(vo);
    }

    @ApiOperation(value = "新增菜单")
    @PostMapping(value = "/menu/saveMenu",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBody saveMenu(@RequestBody MenuDTO dto){
        userService.saveMenu(dto);
        return new ResultBody();
    }

    @ApiOperation(value = "根据条件获取菜单列表")
    @PostMapping(value = "/menu/listMenu",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBody listMenu(@RequestBody MenuListDTO dto){
        PageVO pageVO = userService.listMenu(dto);
        return new ResultBody(pageVO);
    }

    @ApiOperation(value = "修改菜单")
    @PostMapping(value = "/menu/updateMenu",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBody updateMenu(@RequestBody MenuDTO dto){
        userService.updateMenu(dto);
        return new ResultBody();
    }

    @ApiOperation(value = "删除菜单")
    @GetMapping(value = "/menu/deleteMenu")
    public ResultBody deleteMenu(@RequestParam Long key){
        userService.deleteMenu(key);
        return new ResultBody();
    }

    @ApiOperation(value = "获取所有菜单")
    @GetMapping(value = "/menu/getAllMenu")
    public ResultBody getAllMenu(){
        List<MenuVO> list = userService.getAllMenu();
        return new ResultBody(list);
    }

    @ApiOperation(value = "获取所有权限")
    @GetMapping(value = "/permission/getAllPermission")
    public ResultBody getAllPermission(){
        List<PermissionVO> list = userService.getAllPermission();
        return new ResultBody(list);
    }

    @ApiOperation(value = "获取所有权限树形结构")
    @GetMapping(value = "/permission/getAllPermissionTree")
    public ResultBody getAllPermissionTree(){
        List<PermissionTreeVO> list = userService.getAllPermissionTree();
        return new ResultBody(list);
    }

    @ApiOperation(value = "根据权限类型和id查询相应信息")
    @PostMapping(value = "/permission/getPermission")
    public ResultBody getPermission(@RequestBody PermissionGetDTO dto){
        BaseVO vo = userService.getPermission(dto);
        return new ResultBody(vo.getMap());
    }

    @ApiOperation(value = "新增权限")
    @PostMapping(value = "/permission/savePermission")
    public ResultBody savePermission(@RequestBody PermissionUpdateDTO dto){
        userService.savePermission(dto);
        return new ResultBody();
    }

    @ApiOperation(value = "修改权限")
    @PostMapping(value = "/permission/updatePermission")
    public ResultBody updatePermission(@RequestBody PermissionUpdateDTO dto){
        userService.updatePermission(dto);
        return new ResultBody();
    }

    @ApiOperation(value = "修改权限")
    @GetMapping(value = "/permission/deletePermission")
    public ResultBody deletePermission(@RequestParam Long key){
        userService.deletePermission(key);
        return new ResultBody();
    }


}
