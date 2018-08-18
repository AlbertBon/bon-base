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
 * 实体类对应的数据表为：  sys_role_permission
 */
@ApiModel(value ="SysRolePermission")
public class SysRolePermission implements Serializable {
    @Id
    @GeneratedValue(generator = "JDBC")
    @ApiModelProperty(value = "ID")
    private Long sysRolePermissionId;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;

    @ApiModelProperty(value = "最后一次更新时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModified;

    @ApiModelProperty(value = "权限id")
    private Long sysPermissionId;

    @ApiModelProperty(value = "角色id")
    private Long sysRoleId;

    private static final long serialVersionUID = 1L;

    public SysRolePermission(Long sysRolePermissionId, Date gmtCreate, Date gmtModified, Long sysPermissionId, Long sysRoleId) {
        this.sysRolePermissionId = sysRolePermissionId;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
        this.sysPermissionId = sysPermissionId;
        this.sysRoleId = sysRoleId;
    }

    public SysRolePermission() {
        super();
    }

    public Long getSysRolePermissionId() {
        return sysRolePermissionId;
    }

    public void setSysRolePermissionId(Long sysRolePermissionId) {
        this.sysRolePermissionId = sysRolePermissionId;
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

    public Long getSysPermissionId() {
        return sysPermissionId;
    }

    public void setSysPermissionId(Long sysPermissionId) {
        this.sysPermissionId = sysPermissionId;
    }

    public Long getSysRoleId() {
        return sysRoleId;
    }

    public void setSysRoleId(Long sysRoleId) {
        this.sysRoleId = sysRoleId;
    }
}