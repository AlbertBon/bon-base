package com.bon.modules.sys.dao;

import com.bon.modules.sys.domain.entity.User;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {
    User getById(Long userId);
}