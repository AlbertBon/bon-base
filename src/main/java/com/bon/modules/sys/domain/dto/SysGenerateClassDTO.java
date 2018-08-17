package com.bon.modules.sys.domain.dto;

import com.bon.common.domain.dto.BaseDTO;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @program: bon基础项目
 * @description: 系统创建数据表参数
 * @author: Bon
 * @create: 2018-07-13 14:24
 **/
public class SysGenerateClassDTO extends BaseDTO{
    @ApiModelProperty(value = "表名")
    private List<String> tableNameList;

    @ApiModelProperty(value = "所属模块")
    private String modules;

    public List<String> getTableNameList() {
        return tableNameList;
    }

    public void setTableNameList(List<String> tableNameList) {
        this.tableNameList = tableNameList;
    }

    public String getModules() {
        return modules;
    }

    public void setModules(String modules) {
        this.modules = modules;
    }
}
