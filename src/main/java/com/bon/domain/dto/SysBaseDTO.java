package com.bon.domain.dto;

import com.bon.common.dto.BaseDTO;
import com.bon.domain.entity.SysBase;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @program: bon基础项目
 * @description: 系统基础表参数
 * @author: Bon
 * @create: 2018-07-12 11:59
 **/
public class SysBaseDTO extends BaseDTO<SysBase> {
    @ApiModelProperty(value = "表名")
    private String tableName;

    @ApiModelProperty(value = "表备注")
    private String tableRemark;

    @ApiModelProperty(value = "字段信息列表")
    List<SysBaseFieldDTO> fieldList;

    public List<SysBaseFieldDTO> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<SysBaseFieldDTO> fieldList) {
        this.fieldList = fieldList;
    }

    public String getTableRemark() {
        return tableRemark;
    }

    public void setTableRemark(String tableRemark) {
        this.tableRemark = tableRemark;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
