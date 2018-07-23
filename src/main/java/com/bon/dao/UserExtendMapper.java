package com.bon.dao;

import com.bon.domain.entity.Menu;
import com.bon.domain.entity.Permission;
import com.bon.domain.entity.Role;
import com.bon.domain.entity.User;
import com.bon.domain.vo.MenuVO;
import com.bon.domain.vo.PermissionVO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Set;

public interface UserExtendMapper {
    /**
     * 根据用户名获取角色
     * @param username
     * @return
     */
    List<Role> getRoleByUsername(String username);

    /**
     * 根据角色标识获取权限
     * @param roleFlag
     * @return
     */
    List<Permission> getPermissionByRoleFlag(String roleFlag);

    /**
     * 根据用户名获取所有菜单
     * @param username
     * @return
     */
    List<Menu> getMenuByUsername(String username);

    /**
     * 根据菜单id获取菜单视图（包括父节点名称）
     * @param userId
     * @return
     */
    MenuVO getMenuByMenuId(Long userId);
    /**
     * 根据用户id获取所有菜单
     * @param userId
     * @return
     */
    List<Menu> getMenuByUserId(Long userId);

    /**
     * 获取所有菜单视图（不获取父菜单）
     * @return
     */
    List<MenuVO> getAllMenu();

    /**
     * 获取所有权限视图
     * @return
     */
    List<PermissionVO> getAllPermission();
}