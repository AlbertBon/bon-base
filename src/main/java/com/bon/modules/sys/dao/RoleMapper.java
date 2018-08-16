package com.bon.modules.sys.dao;

import com.bon.modules.sys.domain.entity.Role;
import tk.mybatis.mapper.common.Mapper;

public interface RoleMapper extends Mapper<Role> {
    Role getById(Long roleId);
}