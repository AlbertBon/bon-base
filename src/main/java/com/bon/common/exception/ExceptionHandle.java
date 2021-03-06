package com.bon.common.exception;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.bon.common.enums.ExceptionType;
import com.bon.common.vo.ResultBody;
import com.bon.util.MyLog;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: bon基础项目
 * @description: 异常拦截
 * @author: Bon
 * @create: 2018-05-02 11:07
 **/
@RestControllerAdvice
public class ExceptionHandle {

    private static final MyLog log = MyLog.getLog(ExceptionHandle.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultBody handle(RuntimeException e) {
        /*业务异常*/
        log.error(e, e.getMessage());
        if (e instanceof BusinessException) {
            BusinessException businessException = (BusinessException) e;
            return new ResultBody(businessException.getCode(), businessException.getMessage());
        }
        if (e instanceof UnauthorizedException) {
            return new ResultBody(ExceptionType.LOGIN_AUTHORITY_ERROR);
        }
        if(e instanceof IncorrectCredentialsException){
            return new ResultBody(ExceptionType.USERNAME_OR_PASSWORD_ERROR);
        }
        return new ResultBody(ExceptionType.SYSTEM_ERROR.getCode(), ExceptionType.SYSTEM_ERROR.getMessage());
    }

}
