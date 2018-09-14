package com.bon.common.shiro;

import com.bon.common.domain.enums.ExceptionType;
import com.bon.common.domain.vo.ResultBody;
import com.bon.common.util.GeneratePropertyUtil;
import com.bon.common.util.MyLog;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
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

    public static final String DEFAULT_LOGIN_TYPE_PARAM = "loginType";
    private String loginTypeParamName = DEFAULT_LOGIN_TYPE_PARAM;

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //如果其他过滤器已经,验证失败了,则禁止登陆,不再进行身份验证
        if (request.getAttribute(getFailureKeyAttribute()) != null) {
            return true;
        }
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

    @Override
    protected void setFailureAttribute(ServletRequest request, AuthenticationException ae) {
        request.setAttribute(getFailureKeyAttribute(), ae);
    }

    /**
     * 重写该方法,为了将loginType参数保存到token中
     *
     * @param request  请求
     * @param response 响应
     * @return
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String username = getUsername(request);
        String password = getPassword(request);
        String loginType = getLoginType(request);
        return createToken(username, password, request, response, loginType);
    }

    private AuthenticationToken createToken(String username, String password, ServletRequest request, ServletResponse response, String loginType) {
        boolean rememberMe = isRememberMe(request);
        String host = getHost(request);
        return createToken(username, password, rememberMe, host, loginType);
    }

    private AuthenticationToken createToken(String username, String password, boolean rememberMe, String host, String loginType) {
        return new ShiroToken(username, password, rememberMe, host, loginType);
    }

    private String getLoginType(ServletRequest request) {
        return WebUtils.getCleanParam(request, getLoginTypeParamName());
    }

    public String getLoginTypeParamName() {
        return loginTypeParamName;
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
