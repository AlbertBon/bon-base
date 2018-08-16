package com.bon.common.shiro;

import com.bon.common.domain.enums.ExceptionType;
import com.bon.common.domain.vo.ResultBody;
import com.bon.common.util.PropertyUtil;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @program: bon基础项目
 * @description: 自定义角色权限过滤
 * @author: Bon
 * @create: 2018-07-23 18:10
 **/
public class ShiroFilterPermissionAuthorization extends PermissionsAuthorizationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if (req.getMethod().equals(RequestMethod.OPTIONS.name())) {
            resp.setStatus(HttpStatus.OK.value());
            return true;
        }
        //请求错误拦截
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=utf-8");
        resp.setHeader("Access-Control-Allow-Origin", PropertyUtil.getProperty("corsHost"));
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        OutputStream out = response.getOutputStream();
        out.write(new ResultBody(ExceptionType.LOGIN_AUTHORITY_ERROR).toErrString().getBytes("utf-8"));
        out.close();
        return false;
    }
}
