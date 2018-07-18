package com.bon.dao;

import com.bon.domain.entity.UserRole;
import tk.mybatis.mapper.common.Mapper;

public interface UserRoleMapper extends Mapper<UserRole> {
    UserRole getById(Long userRoleId);
}