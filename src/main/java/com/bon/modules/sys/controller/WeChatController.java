package com.bon.modules.sys.controller;

import com.bon.common.domain.vo.ResultBody;
import com.bon.modules.sys.service.WeChatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: base
 * @description: 微信端控制层
 * @author: Bon
 * @create: 2018-09-10 15:42
 **/
@Api(value = "微信端控制层",description = "微信端控制层")
@RequestMapping("/wechat")
@RestController
public class WeChatController {

    @Autowired
    private WeChatService weChatService;

    @ApiOperation(value = "获取openid",notes = "获取openid")
    @GetMapping(value = "/login")
    public ResultBody login(@RequestParam String code){
        String token = weChatService.wechatLogin(code);
        return new ResultBody();
    }
}
