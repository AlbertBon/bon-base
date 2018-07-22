package com.bon.dao;

import com.bon.domain.entity.Menu;
import com.bon.domain.entity.Permission;
import com.bon.domain.entity.Role;
import com.bon.domain.entity.User;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Set;

public interface UserExtendMapper {
    List<Role> getRoleByUsername(String username);
    List<Permission> getPermissionByRoleFlag(String roleFlag);
    List<Menu> getMenuByUsername(String username);
}