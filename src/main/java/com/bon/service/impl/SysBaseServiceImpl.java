package com.bon.service.impl;

import com.bon.common.dto.BaseDTO;
import com.bon.dao.SysBaseMapper;
import com.bon.domain.dto.SysBaseDTO;
import com.bon.domain.entity.SysBase;
import com.bon.domain.vo.SysBaseTablesVO;
import com.bon.domain.vo.SysBaseVO;
import com.bon.service.SysBaseService;
import com.bon.util.BeanUtil;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.javassist.runtime.DotClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: bon基础项目
 * @description: 系统基础表接口实现类
 * @author: Bon
 * @create: 2018-07-12 11:52
 **/
@Service
public class SysBaseServiceImpl implements SysBaseService {

    @Autowired
    SysBaseMapper sysBaseMapper;

    @Override
    public List<SysBaseVO> listTables() {
        List<SysBase> list = sysBaseMapper.listTables();
        List<SysBaseVO> voList = new ArrayList<>();
        for (SysBase sysBase : list) {
            SysBaseVO vo = new SysBaseVO();
            BeanUtil.copyPropertys(sysBase, vo);
            voList.add(vo);
        }
        return voList;
    }

    @Override
    public List<SysBaseVO> listByTableName(SysBaseDTO dto) {
        dto.andFind("tableName",dto.getTableName());
        List<SysBase> list = sysBaseMapper.selectByExample(dto.getExample());
        List<SysBaseVO> voList = new ArrayList<>();
        for (SysBase sysBase : list) {
            SysBaseVO vo = new SysBaseVO();
            BeanUtil.copyPropertys(sysBase, vo);
            voList.add(vo);
        }
        return voList;
    }

    @Override
    public void saveTable(SysBaseDTO dto) {

    }
}
