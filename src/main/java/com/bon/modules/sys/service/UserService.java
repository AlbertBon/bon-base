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
 * @description: 用户、角色、权限管理模块
 * @author: Bon
 * @create: 2018-04-27 17:47
 **/
public interface UserService {
    UserVO getUser(Long id);
    void saveUser(UserDTO userDTO);
    void updateUser(UserDTO userDTO);
    void deleteUser(Long id);
    PageVO listUser(UserListDTO userListDTO);
    List<UserVO> getAllUser();

    /**
     * 根据用户名获取用户
     * @param username
     * @return
     */
    SysUser getUserByUsername(String username);

    /**
     * 根据用户名获取菜单，并组装前端路由格式json
     * @param userId
     * @return
     */
    List<MenuRouterVO> getMenuRouter(String username);

    /**
     * 保存用户角色
     * @param roleIds
     * @param userId
     */
    void saveUserRole(List<Long> roleIds,Long userId);
    /**
     * 获取用户角色id列表
     * @param userId
     * @return
     */
    List<Long> getUserRoleIds(Long userId);

}
