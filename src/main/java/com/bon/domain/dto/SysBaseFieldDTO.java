package com.bon.domain.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * @program: bon基础项目
 * @description: 系统基础表字段参数
 * @author: Bon
 * @create: 2018-07-12 17:16
 **/
public class SysBaseFieldDTO {
    @ApiModelProperty(value = "ID")
    private Long sysBaseId;

    @ApiModelProperty(value = "字段名")
    private String fieldName;

    @ApiModelProperty(value = "字段类型")
    private String fieldType;

    @ApiModelProperty(value = "字段长度")
    private Integer fieldLength;

    @ApiModelProperty(value = "00:是，01：否；是否可以为空")
    private String isNull;

    @ApiModelProperty(value = "00:是，01：否；是否唯一")
    private String isUnique;

    @ApiModelProperty(value = "默认值")
    private String defaultValue;

    @ApiModelProperty(value = "字段备注")
    private String fieldRemark;

    public Long getSysBaseId() {
        return sysBaseId;
    }

    public void setSysBaseId(Long sysBaseId) {
        this.sysBaseId = sysBaseId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public Integer getFieldLength() {
        return fieldLength;
    }

    public void setFieldLength(Integer fieldLength) {
        this.fieldLength = fieldLength;
    }

    public String getIsNull() {
        return isNull;
    }

    public void setIsNull(String isNull) {
        this.isNull = isNull;
    }

    public String getIsUnique() {
        return isUnique;
    }

    public void setIsUnique(String isUnique) {
        this.isUnique = isUnique;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getFieldRemark() {
        return fieldRemark;
    }

    public void setFieldRemark(String fieldRemark) {
        this.fieldRemark = fieldRemark;
    }
}
