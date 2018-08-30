package com.bon.common.shiro;

import com.bon.common.domain.enums.ExceptionType;
import com.bon.common.domain.vo.ResultBody;
import com.bon.common.util.GeneratePropertyUtil;
import com.bon.common.util.MyLog;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * @program: bon基础项目
 * @description: 自定义过滤器
 * @author: Bon
 * @create: 2018-07-23 17:30
 **/
public class ShiroFilterFormAuthentication extends FormAuthenticationFilter {
    private static final MyLog log = MyLog.getLog(ShiroFilterFormAuthentication.class);
    private String corsHost = GeneratePropertyUtil.getProperty("corsHost");
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (this.isLoginRequest(request, response)) {
            if (this.isLoginSubmission(request, response)) {
                if (log.isTraceEnabled()) {
                    log.trace("Login submission detected.  Attempting to execute login.");
                }

                return this.executeLogin(request, response);
            } else {
                if (log.isTraceEnabled()) {
                    log.trace("Login page view.");
                }

                return true;
            }
        } else {
            HttpServletRequest req = (HttpServletRequest)request;
            HttpServletResponse resp = (HttpServletResponse) response;
            if(req.getMethod().equals(RequestMethod.OPTIONS.name())) {
                resp.setStatus(HttpStatus.OK.value());
                return true;
            }
            if (log.isTraceEnabled()) {
                log.trace("Attempting to access a path which requires authentication.  Forwarding to the Authentication url [" + this.getLoginUrl() + "]");
            }
            //请求错误拦截
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json; charset=utf-8");
            resp.setHeader("Access-Control-Allow-Origin", corsHost);
            resp.setHeader("Access-Control-Allow-Credentials","true");
            resp.setHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept");
            OutputStream out = response.getOutputStream();
            out.write(new ResultBody(ExceptionType.EXPIRED_ERROR).toErrString().getBytes("utf-8"));
            out.close();

            return false;
        }
    }

//    @Override
//    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
//                                     ServletResponse response) throws Exception {
//        //获取已登录的用户信息
//        String username =  subject.getPrincipal().toString();
//        //把用户信息保存到session
//        subject.getSession().setAttribute("activeUser", username);
//        return super.onLoginSuccess(token, subject, request, response);
//    }
}
