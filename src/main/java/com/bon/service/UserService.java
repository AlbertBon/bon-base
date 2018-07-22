package com.bon.service;


import com.bon.domain.entity.Permission;
import com.bon.domain.entity.Role;
import com.bon.domain.entity.User;
import com.bon.domain.vo.MenuRouterVO;
import com.bon.domain.vo.UserVO;

import java.util.List;
import java.util.Set;

/**
 * @program: bon-dubbo
 * @description: 用户管理模块
 * @author: Bon
 * @create: 2018-04-27 17:47
 **/
public interface UserService {
    UserVO getUser(Long id);
    User getUserByUsername(String username);

    /**
     * 根据用户名获取角色
     * @param username
     * @return
     */
    List<Role> getRoleByUsername(String username);

    /**
     * 根据角色标识获取
     * @param roleFlag
     * @return
     */
    List<Permission> getPermissionByRoleFlag(String roleFlag);

    /**
     * 根据用户名获取菜单，并组装前端路由格式json
     * @param userId
     * @return
     */
    List<MenuRouterVO> getMenuRouter(String username);


}
