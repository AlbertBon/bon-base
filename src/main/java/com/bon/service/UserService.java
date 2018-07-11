package com.bon.service;


import com.bon.domain.vo.UserVO;

import java.util.List;

/**
 * @program: bon-dubbo
 * @description: 用户管理模块
 * @author: Bon
 * @create: 2018-04-27 17:47
 **/
public interface UserService {
    UserVO getUser(Long id);


}
