package com.bon.controller;

import com.bon.common.vo.ResultBody;
import com.bon.domain.dto.SysBaseDTO;
import com.bon.domain.dto.SysBaseDeleteDTO;
import com.bon.domain.dto.SysGenerateTableDTO;
import com.bon.domain.vo.SysBaseVO;
import com.bon.service.SysBaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: bon基础项目
 * @description: 系统基础表控制器
 * @author: Bon
 * @create: 2018-07-12 11:50
 **/
@Api("系统表管理模块")
@RestController
@RequestMapping("/sys")
public class SysBaseController {
    @Autowired
    private SysBaseService sysBaseService;

    @ApiOperation(value = "获取所有表名信息")
    @GetMapping(value = "/listTables")
    public ResultBody listTables(){
        List<SysBaseVO> vo = sysBaseService.listTables();
        return new ResultBody(vo);
    }

    @ApiOperation(value = "获取所有表结构")
    @PostMapping(value = "/listByTableName")
    public ResultBody listByTableName(@RequestBody SysBaseDTO dto){
        List<SysBaseVO> vo = sysBaseService.listByTableName(dto);
        return new ResultBody(vo);
    }

    @ApiOperation(value = "保存表结构")
    @PostMapping(value = "/saveTable")
    public ResultBody saveTable(@RequestBody SysBaseDTO dto){
        sysBaseService.saveTable(dto);
        return new ResultBody();
    }

    @ApiOperation(value = "根据id删除表字段数据")
    @PostMapping(value = "/deleteField")
    public ResultBody deleteField(@RequestBody SysBaseDeleteDTO dto){
        sysBaseService.deleteField(dto);
        return new ResultBody();
    }

    @ApiOperation(value = "根据id删除表")
    @PostMapping(value = "/deleteTable")
    public ResultBody deleteTable(@RequestBody SysBaseDeleteDTO dto){
        sysBaseService.deleteTable(dto);
        return new ResultBody();
    }

    @ApiOperation(value = "创建表")
    @PostMapping(value = "/generateTable")
    public ResultBody generateTable(@RequestBody SysGenerateTableDTO dto){
        sysBaseService.generateTable(dto);
        return new ResultBody();
    }

    @ApiOperation(value = "创建表")
    @PostMapping(value = "/dropTable")
    public ResultBody dropTable(@RequestBody SysGenerateTableDTO dto){
        sysBaseService.dropTable(dto);
        return new ResultBody();
    }


}
