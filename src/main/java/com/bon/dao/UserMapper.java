package com.bon.dao;

import com.bon.domain.entity.User;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {
    User getById(Long userId);
}