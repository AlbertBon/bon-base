package com.bon.modules.sys.controller;

import com.bon.common.config.Constants;
import com.bon.common.domain.enums.ExceptionType;
import com.bon.common.service.RedisService;
import com.bon.common.domain.vo.ResultBody;
import com.bon.modules.sys.domain.dto.LoginDTO;
import com.bon.modules.sys.domain.dto.TokenDTO;
import com.bon.modules.sys.domain.vo.LoginVO;
import com.bon.modules.sys.domain.vo.TokenVO;
import com.bon.modules.sys.service.LoginService;
import com.bon.common.util.ImageCodeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * @program: bon基础项目
 * @description: 登录
 * @author: Bon
 * @create: 2018-05-16 11:10
 **/
@RestController
@Api(value = "登录管理模块",description = "登录管理模块")
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private RedisService redisService;

    @ApiOperation(value = "登录")
    @PostMapping(value = "/loginIn",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBody loginIn(@RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        LoginVO loginVO=loginService.loginIn(loginDTO);
        return new ResultBody(loginVO);
    }

    @ApiOperation(value = "生成验证码")
    @GetMapping(value = "/getImageCode")
    public void getImageCode(HttpServletRequest request,HttpServletResponse response) throws IOException {
        // 设置响应的类型格式为图片格式
        response.setContentType("image/jpeg");
        //禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        ImageCodeUtil vCode = new ImageCodeUtil(120,40,4,100);

        String sessionId = SecurityUtils.getSubject().getSession().getId().toString();
        String key= MessageFormat.format(Constants.RedisKey.LOGIN_CAPTCHA_SESSION_ID,sessionId);
        //redis存储验证码
        redisService.set(key,vCode.getCode());
        vCode.write(response.getOutputStream());
    }

    @ApiOperation(value = "获取token")
    @PostMapping(value = "/getToken",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBody getToken(@RequestBody TokenDTO dto) throws IOException {
        TokenVO vo = loginService.getToken(dto);
        return new ResultBody(vo);
    }

    @ApiOperation(value = "登出")
    @PostMapping(value = "/loginOut",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBody loginOut(HttpServletRequest request) throws IOException {
        loginService.loginOut();
        return new ResultBody();
    }

    @ApiOperation(value = "没有认证")
    @GetMapping(value = "/unauth",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBody unauth(HttpServletRequest request) throws IOException {
        return new ResultBody(ExceptionType.LOGIN_AUTHORITY_ERROR);
    }

}
