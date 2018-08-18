package com.bon.modules.sys.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @program: bon基础项目
 * @description: 系统基础表信息
 * @author: Bon
 * @create: 2018-07-12 12:00
 **/
public class SysBaseVO {
    @ApiModelProperty(value = "ID")
    private Long baseId;

    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;

    @ApiModelProperty(value = "最后一次更新时间")
    private Date gmtModified;

    @ApiModelProperty(value = "表名")
    private String tableName;

    @ApiModelProperty(value = "表备注")
    private String tableRemark;

    @ApiModelProperty(value = "字段名")
    private String fieldName;

    @ApiModelProperty(value = "字段类型")
    private String fieldType;

    @ApiModelProperty(value = "字段长度")
    private Integer fieldLength;

    @ApiModelProperty(value = "1:是，0：否；是否可以为空")
    private Byte isNull;

    @ApiModelProperty(value = "1:是，0：否；是否唯一")
    private Byte isUnique;

    @ApiModelProperty(value = "1:是，0：否；是否为无符号")
    private Byte isUnsigned;

    @ApiModelProperty(value = "默认值")
    private String defaultValue;

    @ApiModelProperty(value = "字段备注")
    private String fieldRemark;

    @ApiModelProperty(value = "1:是，0：否；是否为id")
    private Byte isId;

    @ApiModelProperty(value = "模块名称")
    private String modules;

    public String getModules() {
        return modules;
    }

    public void setModules(String modules) {
        this.modules = modules;
    }

    public Long getBaseId() {
        return baseId;
    }

    public void setBaseId(Long baseId) {
        this.baseId = baseId;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
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

    public Byte getIsNull() {
        return isNull;
    }

    public void setIsNull(Byte isNull) {
        this.isNull = isNull;
    }

    public Byte getIsUnique() {
        return isUnique;
    }

    public void setIsUnique(Byte isUnique) {
        this.isUnique = isUnique;
    }

    public Byte getIsUnsigned() {
        return isUnsigned;
    }

    public void setIsUnsigned(Byte isUnsigned) {
        this.isUnsigned = isUnsigned;
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

    public Byte getIsId() {
        return isId;
    }

    public void setIsId(Byte isId) {
        this.isId = isId;
    }
}
