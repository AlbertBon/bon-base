package com.bon.modules.sys.domain.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
/**
 * 
 * 实体类对应的数据表为：  sys_base
 */
@ApiModel(value ="SysBase")
public class SysBase implements Serializable {
    @Id
    @GeneratedValue(generator = "JDBC")
    @ApiModelProperty(value = "ID")
    private Long baseId;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;

    @ApiModelProperty(value = "最后一次更新时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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

    private static final long serialVersionUID = 1L;

    public SysBase(Long baseId, Date gmtCreate, Date gmtModified, String tableName, String tableRemark, String fieldName, String fieldType, Integer fieldLength, Byte isNull, Byte isUnique, Byte isUnsigned, String defaultValue, String fieldRemark, Byte isId, String modules) {
        this.baseId = baseId;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
        this.tableName = tableName;
        this.tableRemark = tableRemark;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.fieldLength = fieldLength;
        this.isNull = isNull;
        this.isUnique = isUnique;
        this.isUnsigned = isUnsigned;
        this.defaultValue = defaultValue;
        this.fieldRemark = fieldRemark;
        this.isId = isId;
        this.modules = modules;
    }

    public SysBase() {
        super();
    }

    public Long getBaseId() {
        return baseId;
    }

    public void setBaseId(Long baseId) {
        this.baseId = baseId;
    }

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
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
        this.tableName = tableName == null ? null : tableName.trim();
    }

    public String getTableRemark() {
        return tableRemark;
    }

    public void setTableRemark(String tableRemark) {
        this.tableRemark = tableRemark == null ? null : tableRemark.trim();
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName == null ? null : fieldName.trim();
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType == null ? null : fieldType.trim();
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
        this.defaultValue = defaultValue == null ? null : defaultValue.trim();
    }

    public String getFieldRemark() {
        return fieldRemark;
    }

    public void setFieldRemark(String fieldRemark) {
        this.fieldRemark = fieldRemark == null ? null : fieldRemark.trim();
    }

    public Byte getIsId() {
        return isId;
    }

    public void setIsId(Byte isId) {
        this.isId = isId;
    }

    public String getModules() {
        return modules;
    }

    public void setModules(String modules) {
        this.modules = modules == null ? null : modules.trim();
    }
}