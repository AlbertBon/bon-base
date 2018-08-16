package com.bon.modules.sys.dao;

import com.bon.modules.sys.domain.entity.Permission;
import tk.mybatis.mapper.common.Mapper;

public interface PermissionMapper extends Mapper<Permission> {
    Permission getById(Long permissionId);
}