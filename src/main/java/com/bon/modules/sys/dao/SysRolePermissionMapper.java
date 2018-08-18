package com.bon.modules.sys.dao;

import com.bon.modules.sys.domain.entity.SysRolePermission;
import tk.mybatis.mapper.common.Mapper;

public interface SysRolePermissionMapper extends Mapper<SysRolePermission> {
    SysRolePermission getById(Long rolePermissionId);
}