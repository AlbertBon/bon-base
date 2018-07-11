package com.bon.controller;

import com.bon.common.vo.PageVO;
import com.bon.common.vo.ResultBody;
import com.bon.domain.dto.*;
import com.bon.domain.vo.UserVO;
import com.bon.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: bon-dubbo
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
    @ApiResponse(code = 200, message = "success")
    @GetMapping(value = "/user/getUser")
    public ResultBody getUser(@RequestParam Long key){
        UserVO vo= userService.getUser(key);
        return new ResultBody(vo);
    }

}
