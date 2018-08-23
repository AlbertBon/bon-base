package com.bon.modules.sys.service;

import com.bon.modules.sys.domain.dto.SysBaseDTO;
import com.bon.modules.sys.domain.dto.SysBaseDeleteDTO;
import com.bon.modules.sys.domain.dto.SysGenerateClassDTO;
import com.bon.modules.sys.domain.dto.SysGenerateTableDTO;
import com.bon.modules.sys.domain.vo.SysBaseVO;

import java.io.File;
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

    /**
     * @Author: Bon
     * @Description: 根据excel文件创建数据库
     * @param file
     * @return: void
     * @Date: 2018/8/10 9:57
     */
    void generateTable(File file,List<String> tableList);

    /**
     * @Author: Bon
     * @Description: 根据excel文件创建数据库语句
     * @param file
     * @return: void
     * @Date: 2018/8/10 9:57
     */
    String generateTableSQL(File file,List<String> tableList) throws Exception;

    /**
     * 根据系统表创建数据库表
     * @param dto
     */
    void generateTable(SysGenerateTableDTO dto);

    /**
     * 根据系统表创建mapper和类
     * @param dto
     */
    void generateClass(List<SysGenerateClassDTO> dtoList);

    /**
     * 删除表字段
     * @param dto
     */
    void deleteField(SysBaseDeleteDTO dto);

    /**
     * 删除整张表
     * @param dto
     */
    void deleteTable(SysBaseDeleteDTO dto);

    /**
     * 根据销毁数据库表
     * @param dto
     */
    void dropTable(SysGenerateTableDTO dto);
}
