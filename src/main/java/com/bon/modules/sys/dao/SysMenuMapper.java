package com.bon.modules.sys.dao;

import com.bon.modules.sys.domain.entity.SysMenu;
import tk.mybatis.mapper.common.Mapper;

public interface SysMenuMapper extends Mapper<SysMenu> {
    SysMenu getById(Long menuId);
}