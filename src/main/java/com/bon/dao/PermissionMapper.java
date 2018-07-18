package com.bon.dao;

import com.bon.domain.entity.Permission;
import tk.mybatis.mapper.common.Mapper;

public interface PermissionMapper extends Mapper<Permission> {
    Permission getById(Long permissionId);
}