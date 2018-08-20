package com.bon.modules.sys.controller;

import com.bon.common.config.OperateInitConfig;
import com.bon.common.domain.vo.PageVO;
import com.bon.common.domain.vo.ResultBody;
import com.bon.modules.sys.domain.dto.*;
import com.bon.modules.sys.domain.vo.*;
import com.bon.modules.sys.service.RoleService;
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
 * @description: 角色管理模块
 * @author: Bon
 * @create: 2018-04-27 18:16
 **/
@Api(value = "角色管理模块",description = "角色管理模块")
@RequestMapping("/role")
@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "获取角色",notes = "")
    @GetMapping(value = "/getRole")
    @RequiresPermissions({"url:user:getRole"})
    public ResultBody getRole(@RequestParam Long key){
        RoleVO vo= roleService.getRole(key);
        return new ResultBody(vo);
    }

    @ApiOperation(value = "新增角色",notes = "")
    @PostMapping(value = "/saveRole",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions({"url:user:saveRole"})
    public ResultBody saveRole(@RequestBody RoleDTO dto){
        roleService.saveRole(dto);
        return new ResultBody();
    }

    @ApiOperation(value = "获取角色列表",notes = "根据条件获取角色列表")
    @PostMapping(value = "/listRole",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions({"url:user:listRole"})
    public ResultBody listRole(@RequestBody RoleListDTO dto){
        PageVO pageVO = roleService.listRole(dto);
        return new ResultBody(pageVO);
    }

    @ApiOperation(value = "修改角色",notes = "")
    @ApiResponse(code = 200, message = "success")
    @PostMapping(value = "/updateRole",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions({"url:user:updateRole"})
    public ResultBody updateRole(@RequestBody RoleDTO dto){
        roleService.updateRole(dto);
        return new ResultBody();
    }

    @ApiOperation(value = "删除角色",notes = "")
    @GetMapping(value = "/deleteRole")
    @RequiresPermissions({"url:user:deleteRole"})
    public ResultBody deleteRole(@RequestParam Long key){
        roleService.deleteRole(key);
        return new ResultBody();
    }

    @ApiOperation(value = "获取所有角色",notes = "")
    @GetMapping(value = "/getAllRole")
    @RequiresPermissions({"url:user:getAllRole"})
    public ResultBody getAllRole(){
        List<RoleVO> list = roleService.getAllRole();
        return new ResultBody(list);
    }
}
