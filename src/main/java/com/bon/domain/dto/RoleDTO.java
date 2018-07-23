package com.bon.domain.dto;

import com.bon.common.dto.BaseDTO;
import com.bon.domain.entity.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 实体类对应的数据表为：  role
 * @author null
 * @date 2018-05-25 15:57:59
 */
@ApiModel(value ="Role")
public class RoleDTO extends BaseDTO<Role> {
    @ApiModelProperty(value = "ID")
    private Long roleId;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "角色标识")
    private String roleFlag;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    public String getRoleFlag() {
        return roleFlag;
    }

    public void setRoleFlag(String roleFlag) {
        this.roleFlag = roleFlag == null ? null : roleFlag.trim();
    }
}