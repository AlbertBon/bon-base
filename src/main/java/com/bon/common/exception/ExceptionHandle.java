package com.bon.common.exception;

import com.alibaba.fastjson.JSONObject;
import com.bon.common.enums.ExceptionType;
import com.bon.common.vo.ResultBody;
import com.bon.util.MyLog;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
    public JSONObject handle(RuntimeException e) {
        /*业务异常*/
        LOG.error(e,e.getMessage());
        if(e instanceof BusinessException){
            BusinessException businessException = (BusinessException) e;
            return new ResultBody(businessException.getCode(), businessException.getMessage()).toJsonObject();
        }
        return new ResultBody(ExceptionType.SYSTEM_ERROR.getCode(),ExceptionType.SYSTEM_ERROR.getMessage()).toJsonObject();
    }
}
