package com.bon.common.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @program: bon基础项目
 * @description: 自定义token
 * @author: Bon
 * @create: 2018-07-19 14:09
 **/
public class ShiroToken extends UsernamePasswordToken {
    //验证码
    private String vCode;

    private String loginType;

    public ShiroToken(String username, String password, String vCode, boolean rememberMe) {
        super(username, (char[]) (password != null ? password.toCharArray() : null), rememberMe, (String) null);
        this.vCode = vCode;
    }

    public ShiroToken(String username, String password, String vCode) {
        super(username, (char[]) (password != null ? password.toCharArray() : null), false, (String) null);
        this.vCode = vCode;
    }

    public ShiroToken(String username, String password, boolean rememberMe, String host, String loginType) {
        super(username, password, rememberMe, host);
        this.loginType = loginType;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getvCode() {
        return vCode;
    }

    public void setvCode(String vCode) {
        this.vCode = vCode;
    }
}
