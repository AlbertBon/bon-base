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
@Api(value = "用户管理模块",description = "用户管理模块")
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "获取用户",notes = "根据id获取用户信息")
    @GetMapping(value = "/getUser")
    @RequiresPermissions({"url:user:getUser"})
    public ResultBody getUser(@RequestParam Long key){
        UserVO vo= userService.getUser(key);
        return new ResultBody(vo);
    }

    @ApiOperation(value = "新增用户",notes = "")
    @PostMapping(value = "/saveUser",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions({"url:user:saveUser"})
    public ResultBody saveUser(@RequestBody UserDTO user){
        userService.saveUser(user);
        return new ResultBody();
    }

    @ApiOperation(value = "获取用户列表",notes = "根据条件获取用户列表")
    @PostMapping(value = "/listUser",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions({"url:user:listUser"})
    public ResultBody listUser(@RequestBody UserListDTO listDTO){
        PageVO pageVO = userService.listUser(listDTO);
        return new ResultBody(pageVO);
    }

    @ApiOperation(value = "修改用户",notes = "")
    @PostMapping(value = "/updateUser",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions({"url:user:updateUser"})
    public ResultBody updateUser(@RequestBody UserDTO dto){
        userService.updateUser(dto);
        return new ResultBody();
    }

    @ApiOperation(value = "删除用户",notes = "")
    @GetMapping(value = "/deleteUser")
    @RequiresPermissions({"url:user:deleteUser"})
    public ResultBody deleteUser(@RequestParam Long key){
        userService.deleteUser(key);
        return new ResultBody();
    }

}
