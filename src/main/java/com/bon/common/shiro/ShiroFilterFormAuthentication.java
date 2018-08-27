package com.bon.common.shiro;

import com.bon.common.domain.enums.ExceptionType;
import com.bon.common.domain.vo.ResultBody;
import com.bon.common.util.PropertyUtil;
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
    private String corsHost = PropertyUtil.getProperty("corsHost");
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

//            this.saveRequestAndRedirectToLogin(request, response);
            return false;
        }
//        if (isLoginRequest(request, response)) {
//            if (isLoginSubmission(request, response)) {
//                if (log.isTraceEnabled()) {
//                    log.trace("Login submission detected.  Attempting to execute login.");
//                }
//                return executeLogin(request, response);
//            } else {
//                if (log.isTraceEnabled()) {
//                    log.trace("Login page view.");
//                }
//                //allow them to see the login page ;)
//                return true;
//            }
//        } else {
//            HttpServletRequest req = (HttpServletRequest)request;
//            HttpServletResponse resp = (HttpServletResponse) response;
//            if(req.getMethod().equals(RequestMethod.OPTIONS.name())) {
//                resp.setStatus(HttpStatus.OK.value());
//                return true;
//            }
//
//            if (log.isTraceEnabled()) {
//                log.trace("Attempting to access a path which requires authentication.  Forwarding to the " +
//                        "Authentication url [" + getLoginUrl() + "]");
//            }
//
//            //前端Ajax请求时requestHeader里面带一些参数，用于判断是否是前端的请求
//            if (req.getHeader("axios") != null) {
//                //请求错误拦截
//                resp.setCharacterEncoding("UTF-8");
//                resp.setContentType("application/json; charset=utf-8");
//                resp.setHeader("Access-Control-Allow-Origin", PropertyUtil.getProperty("corsHost"));
//                resp.setHeader("Access-Control-Allow-Credentials","true");
//                resp.setHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept");
//                OutputStream out = response.getOutputStream();
//                out.write(new ResultBody(ExceptionType.EXPIRED_ERROR).toErrString().getBytes("utf-8"));
//                out.close();
//            } else {
//                saveRequestAndRedirectToLogin(request, response);
//            }
//            return false;
//        }
    }

//    @Override
//    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
//        boolean allowed = super.isAccessAllowed(request, response, mappedValue);
//        if (!allowed) {
//            // 判断请求是否是options请求
//            String method = WebUtils.toHttp(request).getMethod();
//            if (StringUtils.equalsIgnoreCase("OPTIONS", method)) {
//                return true;
//            }
//        }
//        return allowed;
//    }
}
