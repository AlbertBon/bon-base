package com.bon.modules.sys.dao;

import com.bon.modules.sys.domain.entity.SysUserRole;
import tk.mybatis.mapper.common.Mapper;

public interface SysUserRoleMapper extends Mapper<SysUserRole> {
    SysUserRole getById(Long userRoleId);
}