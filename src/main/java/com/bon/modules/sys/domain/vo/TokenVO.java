package com.bon.modules.sys.domain.vo;

import java.io.Serializable;

/**
 * @program: bon基础项目
 * @description: token结果信息
 * @author: Bon
 * @create: 2018-05-23 17:58
 **/
public class TokenVO implements Serializable{
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
