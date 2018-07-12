package com.bon.service;

import com.bon.domain.dto.SysBaseDTO;
import com.bon.domain.vo.SysBaseTablesVO;
import com.bon.domain.vo.SysBaseVO;

import java.util.List;

/**
 * @program: bon基础项目
 * @description: 系统基础表接口
 * @author: Bon
 * @create: 2018-07-12 11:51
 **/
public interface SysBaseService {

    /**
     * @Author: Bon
     * @Description: 获取系统中所有表名信息
     * @param
     * @return: java.util.List<com.bon.domain.vo.SysBaseTablesVO>
     * @Date: 2018/7/12 12:32
     */
    List<SysBaseVO> listTables();

    /**
     * @Author: Bon
     * @Description: 根据表名获取系统中表结构信息
     * @param
     * @return: java.util.List<com.bon.domain.vo.SysBaseTablesVO>
     * @Date: 2018/7/12 12:32
     */
    List<SysBaseVO> listByTableName(SysBaseDTO dto);

    /**
     * @Author: Bon
     * @Description: 保存表结构
     * @param
     * @return: void
     * @Date: 2018/7/12 17:23
     */
    void saveTable(SysBaseDTO dto);
}
