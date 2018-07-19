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
 * @program: dubbo-wxmanage
 * @description: 异常拦截
 * @author: Bon
 * @create: 2018-05-02 11:07
 **/
@RestControllerAdvice
public class ExceptionHandle {

    private static final MyLog LOG = MyLog.getLog(ExceptionHandle.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultBody handle(RuntimeException e) {
        /*业务异常*/
        LOG.error(e, e.getMessage());
        if (e instanceof BusinessException) {
            BusinessException businessException = (BusinessException) e;
            return new ResultBody(businessException.getCode(), businessException.getMessage());
        }
        if(e instanceof IncorrectCredentialsException){
            return new ResultBody(ExceptionType.USERNAME_OR_PASSWORD_ERROR);
        }
        return new ResultBody(ExceptionType.SYSTEM_ERROR.getCode(), ExceptionType.SYSTEM_ERROR.getMessage());
    }

//    @Override
//    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception ex) {
//        ModelAndView mv = new ModelAndView();
//        FastJsonJsonView view = new FastJsonJsonView();
//        Map<String, Object> attributes = new HashMap<String, Object>();
//        if (ex instanceof UnauthenticatedException) {
//            attributes.put("code", "1000001");
//            attributes.put("msg", "token错误");
//        } else if (ex instanceof UnauthorizedException) {
//            attributes.put("code", "1000002");
//            attributes.put("msg", "用户无权限");
//        } else {
//            attributes.put("code", "1000003");
//            attributes.put("msg", ex.getMessage());
//        }
//
//        view.setAttributesMap(attributes);
//        mv.setView(view);
//        return mv;
//
//    }
}
