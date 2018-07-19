package com.bon.service.impl;


import com.bon.common.dto.BaseDTO;
import com.bon.common.enums.ExceptionType;
import com.bon.common.exception.BusinessException;
import com.bon.dao.UserMapper;
import com.bon.domain.entity.User;
import com.bon.domain.vo.UserVO;
import com.bon.service.UserService;
import com.bon.util.BeanUtil;
import com.bon.util.MyLog;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: bon-dubbo
 * @description: 用户信息管理模块
 * @author: Bon
 * @create: 2018-04-27 18:00
 **/
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final MyLog LOG = MyLog.getLog(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户
     * @param id
     * @return
     */
    @Override
    public UserVO getUser(Long id) {
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null) {
            throw new BusinessException(ExceptionType.USERNAME_NULL_PASSWORD_ERROR);
        }
        UserVO vo = new UserVO();
        vo = BeanUtil.copyPropertys(user, vo);
//        //放入用户角色id列表信息
//        vo.setRoleIds(getUserRoleIds(user.getUserId()));
        return vo;
    }

    @Override
    public User findByUsername(String username) {
        BaseDTO dto = new BaseDTO();
        dto.andFind(new User(),"username",username);

        return userMapper.selectOneByExample(dto.getExample());
    }


}
