package com.bon.service;

import com.bon.domain.dto.LoginDTO;
import com.bon.domain.dto.TokenDTO;
import com.bon.domain.vo.LoginVO;
import com.bon.domain.vo.TokenVO;

/**
 * @program: dubbo-wxmanage
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
