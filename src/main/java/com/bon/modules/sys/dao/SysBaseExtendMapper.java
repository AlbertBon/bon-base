package com.bon.modules.sys.dao;

import com.bon.modules.sys.domain.entity.SysBase;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SysBaseExtendMapper extends Mapper<SysBase> {
    SysBase getById(Long baseId);
    List<SysBase> listTables();
}