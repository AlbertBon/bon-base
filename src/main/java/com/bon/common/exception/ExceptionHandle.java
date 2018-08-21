package com.bon.common.exception;

import com.bon.common.domain.dto.BaseDTO;
import com.bon.common.domain.enums.ExceptionType;
import com.bon.common.domain.vo.ResultBody;
import com.bon.common.util.MyLog;
import com.bon.common.util.SpringUtil;
import com.bon.modules.sys.dao.SysPermissionMapper;
import com.bon.modules.sys.dao.SysRoleMapper;
import com.bon.modules.sys.domain.entity.SysPermission;
import com.bon.modules.sys.domain.entity.SysRole;
import com.bon.modules.sys.service.PermissionService;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
            String message = e.getMessage();
            String authError = message.substring(message.indexOf("[")+1,message.indexOf("]"));
            if(message.contains("does not have role")){
                //角色错误
                SysRoleMapper roleMapper = SpringUtil.getBean(SysRoleMapper.class);
                BaseDTO baseDTO = new BaseDTO();
                baseDTO.andFind(new SysRole(),"roleFlag",authError);
                SysRole role = roleMapper.selectOneByExample(baseDTO.getExample());
                return new ResultBody(ExceptionType.LOGIN_AUTHORITY_ERROR.getCode(),"您没有--"+role.getRoleName()+"--角色，请联系管理员");
            }else if(message.contains("does not have permission")){
                //权限错误
                SysPermissionMapper permissionMapper = SpringUtil.getBean(SysPermissionMapper.class);
                BaseDTO baseDTO = new BaseDTO();
                baseDTO.andFind(new SysPermission(),"permissionFlag",authError);
                SysPermission permission = permissionMapper.selectOneByExample(baseDTO.getExample());
                return new ResultBody(ExceptionType.LOGIN_AUTHORITY_ERROR.getCode(),"您没有--"+permission.getPermissionName()+"--权限，请联系管理员");
            }

        }
        if(e instanceof IncorrectCredentialsException){
            return new ResultBody(ExceptionType.USERNAME_OR_PASSWORD_ERROR);
        }
        return new ResultBody(ExceptionType.SYSTEM_ERROR.getCode(), ExceptionType.SYSTEM_ERROR.getMessage());
    }

}
