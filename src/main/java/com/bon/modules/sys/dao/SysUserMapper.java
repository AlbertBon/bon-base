package com.bon.modules.sys.dao;

import com.bon.modules.sys.domain.entity.SysUser;
import tk.mybatis.mapper.common.Mapper;

public interface SysUserMapper extends Mapper<SysUser> {
    SysUser getById(Long userId);
}