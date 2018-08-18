package com.bon.modules.sys.dao;

import com.bon.modules.sys.domain.entity.SysRole;
import tk.mybatis.mapper.common.Mapper;

public interface SysRoleMapper extends Mapper<SysRole> {
    SysRole getById(Long roleId);
}