package com.bon.controller;

import com.bon.common.vo.ResultBody;
import com.bon.domain.dto.SysBaseDTO;
import com.bon.domain.dto.SysCreateTableDTO;
import com.bon.domain.vo.SysBaseVO;
import com.bon.domain.vo.UserVO;
import com.bon.service.SysBaseService;
import com.bon.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
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

    @ApiOperation(value = "创建表")
    @PostMapping(value = "/createTable")
    public ResultBody createTable(@RequestBody SysCreateTableDTO dto){
        sysBaseService.createTable(dto);
        return new ResultBody();
    }
}
