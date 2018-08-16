package com.bon.modules.sys.dao;

import com.bon.modules.sys.domain.entity.RolePermission;
import tk.mybatis.mapper.common.Mapper;

public interface RolePermissionMapper extends Mapper<RolePermission> {
    RolePermission getById(Long rolePermissionId);
}