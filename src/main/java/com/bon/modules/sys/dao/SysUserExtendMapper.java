package com.bon.modules.sys.dao;

import com.bon.modules.sys.domain.entity.SysPermission;
import com.bon.modules.sys.domain.entity.SysRole;
import com.bon.modules.sys.domain.entity.SysUser;
import com.bon.modules.sys.domain.vo.PermissionTreeVO;
import com.bon.modules.sys.domain.vo.PermissionVO;
import com.bon.modules.sys.domain.vo.UserVO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Set;

public interface SysUserExtendMapper {
    /**
     * 根据用户名获取角色
     * @param username
     * @return
     */
    List<SysRole> getRoleByUsername(String username);

    /**
     * 根据角色标识获取权限
     * @param roleFlag
     * @return
     */
    List<SysPermission> getPermissionByRoleFlag(String roleFlag);

    /**
     * 根据角色标识获取权限(获取没有子节点的权限)
     * @param roleFlag
     * @return
     */
    List<Long> getPermissionIdsByRoleFlag(String roleFlag);

    /**
     * 根据用户名获取所有菜单,返回的是菜单类型的权限（只返回根节点的权限）
     * @param username
     * @return
     */
    List<SysPermission> getMenuByUsername(String username);

    /**
     * 获取所有权限视图
     * @return
     */
    List<PermissionVO> getAllPermission();

    /**
     * 根据用户名获取用户
     * @param username
     * @return
     */
    SysUser getByUsername(String username);

    /**
     * 获取所有权限标识
     * @return
     */
    List<String> getPermissionFlag();
}