package com.bon.modules.sys.dao;

import com.bon.modules.sys.domain.entity.SysBase;
import tk.mybatis.mapper.common.Mapper;

public interface SysBaseMapper extends Mapper<SysBase> {
    SysBase getById(Long baseId);
}