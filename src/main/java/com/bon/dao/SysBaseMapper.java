package com.bon.dao;

import com.bon.domain.entity.SysBase;
import tk.mybatis.mapper.common.Mapper;

public interface SysBaseMapper extends Mapper<SysBase> {
    SysBase getById(Long sysBaseId);
}