package com.bon.dao;

import com.bon.domain.entity.RolePermission;
import tk.mybatis.mapper.common.Mapper;

public interface RolePermissionMapper extends Mapper<RolePermission> {
    RolePermission getById(Long rolePermissionId);
}