package com.bon.common.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 * @program: bon基础项目
 * @description: shiro 自定义对比密码类(包括校验验证码)
 * @author: Bon
 * @create: 2018-07-19 14:25
 **/
public class ShiroCredentialsMatcher extends SimpleCredentialsMatcher{

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        Object tokenCredentials = this.getCredentials(token);
        Object accountCredentials = this.getCredentials(info);
        return this.equals(tokenCredentials, accountCredentials);
    }
}
