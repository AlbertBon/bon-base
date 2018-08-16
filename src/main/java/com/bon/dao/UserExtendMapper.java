package com.bon.dao;

import com.bon.domain.entity.Menu;
import com.bon.domain.entity.Permission;
import com.bon.domain.entity.Role;
import com.bon.domain.entity.User;
import com.bon.domain.vo.PermissionTreeVO;
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
     * 根据用户名获取所有菜单,返回的是菜单类型的权限（只返回根节点的权限）
     * @param username
     * @return
     */
    List<Permission> getMenuByUsername(String username);

    /**
     * 获取所有权限视图
     * @return
     */
    List<PermissionVO> getAllPermission();
}