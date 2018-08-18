package com.bon.modules.sys.service;


import com.bon.common.domain.vo.PageVO;
import com.bon.modules.sys.domain.dto.*;
import com.bon.modules.sys.domain.entity.SysPermission;
import com.bon.modules.sys.domain.entity.SysRole;
import com.bon.modules.sys.domain.entity.SysUser;
import com.bon.modules.sys.domain.vo.*;

import java.util.List;

/**
 * @program: bon-bon基础项目
 * @description: 用户管理模块
 * @author: Bon
 * @create: 2018-04-27 17:47
 **/
public interface ShiroService {
    /**
     * 根据用户名获取用户
     * @param username
     * @return
     */
    SysUser getUserByUsername(String username);

    /**
     * 根据用户名获取角色
     * @param username
     * @return
     */
    List<SysRole> getRoleByUsername(String username);

    /**
     * 根据角色标识获取
     * @param roleFlag
     * @return
     */
    List<SysPermission> getPermissionByRoleFlag(String roleFlag);

}
