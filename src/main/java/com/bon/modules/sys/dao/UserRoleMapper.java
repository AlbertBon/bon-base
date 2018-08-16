package com.bon.modules.sys.dao;

import com.bon.modules.sys.domain.entity.UserRole;
import tk.mybatis.mapper.common.Mapper;

public interface UserRoleMapper extends Mapper<UserRole> {
    UserRole getById(Long userRoleId);
}