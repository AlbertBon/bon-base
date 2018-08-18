package com.bon.modules.sys.domain.dto;

import com.bon.common.domain.dto.BaseDTO;
import io.swagger.annotations.ApiModelProperty;

/**
 * @program: base
 * @description: delete params
 * @author: Bon
 * @create: 2018-07-15 13:11
 **/
public class SysBaseDeleteDTO extends BaseDTO {
    @ApiModelProperty(value = "ID")
    private Long baseId;

    @ApiModelProperty(value = "表名")
    private String tableName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Long getBaseId() {
        return baseId;
    }

    public void setBaseId(Long baseId) {
        this.baseId = baseId;
    }
}
