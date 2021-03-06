package com.bon.domain.dto;

import com.bon.common.dto.BaseDTO;
import io.swagger.annotations.ApiModelProperty;

/**
 * @program: base
 * @description: delete params
 * @author: Bon
 * @create: 2018-07-15 13:11
 **/
public class SysBaseDeleteDTO extends BaseDTO {
    @ApiModelProperty(value = "ID")
    private Long sysBaseId;

    @ApiModelProperty(value = "表名")
    private String tableName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Long getSysBaseId() {
        return sysBaseId;
    }

    public void setSysBaseId(Long sysBaseId) {
        this.sysBaseId = sysBaseId;
    }
}
