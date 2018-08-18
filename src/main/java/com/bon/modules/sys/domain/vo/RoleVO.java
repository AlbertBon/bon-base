package com.bon.modules.sys.domain.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * 实体类对应的数据表为：  role
 * @author null
 * @date 2018-05-25 15:57:59
 */
public class RoleVO implements Serializable {

    private Long sysRoleId;

    private Date gmtCreate;

    private Date gmtModified;

    private String roleName;

    private String roleFlag;

    private List<Long> sysPermissionIds;

    public List<Long> getPermissionIds() {
        return sysPermissionIds;
    }

    public void setPermissionIds(List<Long> sysPermissionIds) {
        this.sysPermissionIds = sysPermissionIds;
    }

    public Long getRoleId() {
        return sysRoleId;
    }

    public void setRoleId(Long sysRoleId) {
        this.sysRoleId = sysRoleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleFlag() {
        return roleFlag;
    }

    public void setRoleFlag(String roleFlag) {
        this.roleFlag = roleFlag;
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
}