package com.bon.modules.sys.dao;

import com.bon.modules.sys.domain.entity.Menu;
import tk.mybatis.mapper.common.Mapper;

public interface MenuMapper extends Mapper<Menu> {
    Menu getById(Long menuId);
}