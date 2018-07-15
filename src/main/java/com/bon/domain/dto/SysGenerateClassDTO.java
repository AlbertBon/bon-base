package com.bon.domain.dto;

import com.bon.common.dto.BaseDTO;
import io.swagger.annotations.ApiModelProperty;

/**
 * @program: bon基础项目
 * @description: 系统创建数据表参数
 * @author: Bon
 * @create: 2018-07-13 14:24
 **/
public class SysGenerateClassDTO extends BaseDTO{
    @ApiModelProperty(value = "表名")
    private String tableName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
