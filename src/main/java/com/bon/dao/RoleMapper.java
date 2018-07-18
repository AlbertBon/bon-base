package com.bon.dao;

import com.bon.domain.entity.Role;
import tk.mybatis.mapper.common.Mapper;

public interface RoleMapper extends Mapper<Role> {
    Role getById(Long roleId);
}