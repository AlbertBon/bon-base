package com.bon.modules.sys.controller;

import com.bon.common.domain.vo.ResultBody;
import com.bon.modules.sys.domain.dto.SysBaseDTO;
import com.bon.modules.sys.domain.dto.SysBaseDeleteDTO;
import com.bon.modules.sys.domain.dto.SysGenerateTableDTO;
import com.bon.modules.sys.domain.vo.SysBaseVO;
import com.bon.modules.sys.service.SysBaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: bon基础项目
 * @description: 系统基础表控制器
 * @author: Bon
 * @create: 2018-07-12 11:50
 **/
@Api(value = "系统表管理模块",description = "系统表管理模块")
@RestController
@RequestMapping("/sys")
public class SysBaseController {
    @Autowired
    private SysBaseService sysBaseService;

    @ApiOperation(value = "获取所有表名信息",notes = "")
    @GetMapping(value = "/listTables")
    @RequiresPermissions(value = "url:sys:listTables")
    public ResultBody listTables(){
        List<SysBaseVO> vo = sysBaseService.listTables();
        return new ResultBody(vo);
    }

    @ApiOperation(value = "获取所有表结构",notes = "根据表名获取表结构信息")
    @PostMapping(value = "/listByTableName")
    @RequiresPermissions(value = "url:sys:listByTableName")
    public ResultBody listByTableName(@RequestBody SysBaseDTO dto){
        List<SysBaseVO> vo = sysBaseService.listByTableName(dto);
        return new ResultBody(vo);
    }

    @ApiOperation(value = "保存表结构",notes = "在系统基础表中保存表结构信息")
    @PostMapping(value = "/saveTable")
    @RequiresPermissions(value = "url:sys:saveTable")
    public ResultBody saveTable(@RequestBody SysBaseDTO dto){
        sysBaseService.saveTable(dto);
        return new ResultBody();
    }

    @ApiOperation(value = "删除表字段",notes = "根据id删除表某一个字段数据")
    @PostMapping(value = "/deleteField")
    @RequiresPermissions(value = "url:sys:deleteField")
    public ResultBody deleteField(@RequestBody SysBaseDeleteDTO dto){
        sysBaseService.deleteField(dto);
        return new ResultBody();
    }

    @ApiOperation(value = "删除表",notes = "会删除系统基础表中该表的表结构数据，并且会销毁该表在数据库中的数据")
    @PostMapping(value = "/deleteTable")
    @RequiresPermissions(value = "url:sys:deleteTable")
    public ResultBody deleteTable(@RequestBody SysBaseDeleteDTO dto){
        sysBaseService.deleteTable(dto);
        return new ResultBody();
    }

    @ApiOperation(value = "创建表",notes = "根据表信息创建表")
    @PostMapping(value = "/generateTable")
    @RequiresPermissions(value = "url:sys:generateTable")
    public ResultBody generateTable(@RequestBody SysGenerateTableDTO dto){
        sysBaseService.generateTable(dto);
        return new ResultBody();
    }

    @ApiOperation(value = "销毁表",notes = "根据表名销毁表，会删除数据库中该表数据，但不会删除系统基础表中该表的表结构")
    @PostMapping(value = "/dropTable")
    @RequiresPermissions(value = "url:sys:dropTable")
    public ResultBody dropTable(@RequestBody SysGenerateTableDTO dto){
        sysBaseService.dropTable(dto);
        return new ResultBody();
    }


}
