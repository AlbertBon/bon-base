package com.bon.dao;

import com.bon.domain.entity.Menu;
import tk.mybatis.mapper.common.Mapper;

public interface MenuMapper extends Mapper<Menu> {
    Menu getById(Long menuId);
}