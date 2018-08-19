package com.bon.modules.sys.dao;

import com.bon.modules.sys.domain.entity.SysUrl;
import tk.mybatis.mapper.common.Mapper;

public interface SysUrlMapper extends Mapper<SysUrl> {
    SysUrl getById(Long urlId);
}