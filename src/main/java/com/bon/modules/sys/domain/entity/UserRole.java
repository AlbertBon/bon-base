package com.bon.modules.sys.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 实体类对应的数据表为：  user_role
 * @author null
 * @date 2018-05-25 15:57:59
 */
@ApiModel(value ="UserRole")
public class UserRole implements Serializable {
    @Id
    @GeneratedValue(generator = "JDBC")
    @ApiModelProperty(value = "ID")
    private Long userRoleId;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;

    @ApiModelProperty(value = "最后一次更新时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModified;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "角色id")
    private Long roleId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table user_role
     *
     * @mbg.generated 2018-05-25 15:57:59
     */
    private static final long serialVersionUID = 1L;

    public UserRole(Long userRoleId, Date gmtCreate, Date gmtModified, Long userId, Long roleId) {
        this.userRoleId = userRoleId;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
        this.userId = userId;
        this.roleId = roleId;
    }

    public UserRole() {
        super();
    }

    public Long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Long userRoleId) {
        this.userRoleId = userRoleId;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}