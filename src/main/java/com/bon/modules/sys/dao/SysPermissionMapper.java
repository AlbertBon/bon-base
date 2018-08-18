package com.bon.modules.sys.dao;

import com.bon.modules.sys.domain.entity.SysPermission;
import tk.mybatis.mapper.common.Mapper;

public interface SysPermissionMapper extends Mapper<SysPermission> {
    SysPermission getById(Long permissionId);
}