package com.bon.modules.sys.domain.dto;

import com.bon.common.domain.dto.BaseDTO;
import io.swagger.annotations.ApiModelProperty;

/**
 * @program: bon基础项目
 * @description: 系统创建数据表参数
 * @author: Bon
 * @create: 2018-07-13 14:24
 **/
public class SysGenerateTableDTO extends BaseDTO{
    @ApiModelProperty(value = "表名")
    private String tableName;

    @ApiModelProperty(value = "表备注")
    private String tableRemark;

    @ApiModelProperty(value = "是否删除基础数据表的数据；1是0否")
    private Byte isDeleteBase;

    public Byte getIsDeleteBase() {
        return isDeleteBase;
    }

    public void setIsDeleteBase(Byte isDeleteBase) {
        this.isDeleteBase = isDeleteBase;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableRemark() {
        return tableRemark;
    }

    public void setTableRemark(String tableRemark) {
        this.tableRemark = tableRemark;
    }
}
