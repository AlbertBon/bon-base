package com.bon.modules.sys.dao;

import com.bon.modules.sys.domain.entity.Menu;
import com.bon.modules.sys.domain.entity.Permission;
import com.bon.modules.sys.domain.entity.Role;
import com.bon.modules.sys.domain.entity.User;
import com.bon.modules.sys.domain.vo.PermissionTreeVO;
import com.bon.modules.sys.domain.vo.PermissionVO;
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