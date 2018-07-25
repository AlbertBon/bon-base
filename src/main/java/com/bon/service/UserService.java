package com.bon.service;


import com.bon.common.vo.PageVO;
import com.bon.domain.dto.*;
import com.bon.domain.entity.Permission;
import com.bon.domain.entity.Role;
import com.bon.domain.entity.User;
import com.bon.domain.vo.*;

import java.util.List;
import java.util.Set;

/**
 * @program: bon-bon基础项目
 * @description: 用户管理模块
 * @author: Bon
 * @create: 2018-04-27 17:47
 **/
public interface UserService {
    UserVO getUser(Long id);

    /**
     * 根据用户名获取用户
     * @param username
     * @return
     */
    User getUserByUsername(String username);
    void saveUser(UserDTO userDTO);
    void updateUser(UserDTO userDTO);
    void deleteUser(Long id);
    PageVO listUser(UserListDTO userListDTO);
    List<UserVO> getAllUser();

    RoleVO getRole(Long id);
    void saveRole(RoleDTO dto);
    void updateRole(RoleDTO dto);
    void deleteRole(Long id);
    PageVO listRole(RoleListDTO dto);
    List<RoleVO> getAllRole();

    MenuVO getMenu(Long id);
    void saveMenu(MenuDTO dto);
    void updateMenu(MenuDTO dto);
    void deleteMenu(Long id);
    PageVO listMenu(MenuListDTO dto);
    List<MenuVO> getAllMenu();

    List<PermissionVO> getAllPermission();

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

    /**
     * 保存角色权限
     * @param roleIds
     * @param userId
     */
    void saveRolePermission(List<Long> permissionIds,Long roleId);
    /**
     * 获取角色权限id列表
     * @param roleId
     * @return
     */
    List<Long> getRolePermissionIds(Long roleId);



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
