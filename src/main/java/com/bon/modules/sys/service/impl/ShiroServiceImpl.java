package com.bon.modules.sys.service.impl;


import com.bon.common.domain.dto.BaseDTO;
import com.bon.common.domain.enums.ExceptionType;
import com.bon.common.domain.vo.PageVO;
import com.bon.common.exception.BusinessException;
import com.bon.common.util.*;
import com.bon.modules.sys.dao.*;
import com.bon.modules.sys.domain.dto.*;
import com.bon.modules.sys.domain.entity.*;
import com.bon.modules.sys.domain.enums.PermissionType;
import com.bon.modules.sys.domain.vo.*;
import com.bon.modules.sys.service.ShiroService;
import com.bon.modules.sys.service.UserService;
import com.github.pagehelper.PageHelper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @program: bon-bon基础项目
 * @description: 用户信息管理模块
 * @author: Bon
 * @create: 2018-04-27 18:00
 **/
@Service
public class ShiroServiceImpl implements ShiroService {

    private static final MyLog log = MyLog.getLog(ShiroServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysUserExtendMapper userExtendMapper;

    @Override
    public SysUser getUserByUsername(String username) {
        BaseDTO dto = new BaseDTO();
        dto.andFind(new SysUser(),"username",username);

        return userMapper.selectOneByExample(dto.getExample());
    }

    @Override
    public List<SysRole> getRoleByUsername(String username) {
        return userExtendMapper.getRoleByUsername(username);
    }

    @Override
    public List<SysPermission> getPermissionByRoleFlag(String roleFlag) {
        return userExtendMapper.getPermissionByRoleFlag(roleFlag);
    }

}
