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
 * 实体类对应的数据表为：  sys_user_role
 */
@ApiModel(value ="SysUserRole")
public class SysUserRole implements Serializable {
    @Id
    @GeneratedValue(generator = "JDBC")
    @ApiModelProperty(value = "ID")
    private Long sysUserRoleId;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;

    @ApiModelProperty(value = "最后一次更新时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModified;

    @ApiModelProperty(value = "用户id")
    private Long sysUserId;

    @ApiModelProperty(value = "角色id")
    private Long sysRoleId;

    private static final long serialVersionUID = 1L;

    public SysUserRole(Long sysUserRoleId, Date gmtCreate, Date gmtModified, Long sysUserId, Long sysRoleId) {
        this.sysUserRoleId = sysUserRoleId;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
        this.sysUserId = sysUserId;
        this.sysRoleId = sysRoleId;
    }

    public SysUserRole() {
        super();
    }

    public Long getSysUserRoleId() {
        return sysUserRoleId;
    }

    public void setSysUserRoleId(Long sysUserRoleId) {
        this.sysUserRoleId = sysUserRoleId;
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

    public Long getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Long sysUserId) {
        this.sysUserId = sysUserId;
    }

    public Long getSysRoleId() {
        return sysRoleId;
    }

    public void setSysRoleId(Long sysRoleId) {
        this.sysRoleId = sysRoleId;
    }
}