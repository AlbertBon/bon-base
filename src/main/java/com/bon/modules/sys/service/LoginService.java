package com.bon.modules.sys.service;

import com.bon.modules.sys.domain.dto.LoginDTO;
import com.bon.modules.sys.domain.dto.TokenDTO;
import com.bon.modules.sys.domain.vo.LoginVO;
import com.bon.modules.sys.domain.vo.TokenVO;

/**
 * @program: bon基础项目
 * @description: 登录模块
 * @author: Bon
 * @create: 2018-05-16 11:14
 **/
public interface LoginService {
    LoginVO loginIn(LoginDTO loginDTO);
    TokenVO getToken(TokenDTO dto);
    boolean check(String pattern);
    void loginOut();
}
