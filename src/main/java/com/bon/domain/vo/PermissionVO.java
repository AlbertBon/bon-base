package com.bon.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * 
 * 实体类对应的数据表为：  permission
 * @author null
 * @date 2018-06-04 17:58:03
 */
@ApiModel(value ="Permission")
public class PermissionVO implements Serializable {
    @Id
    @ApiModelProperty(value = "ID")
    private Long permissionId;

    @ApiModelProperty(value = "权限名称")
    private String permissionName;

    @ApiModelProperty(value = "00:菜单权限")
    private String type;

    @ApiModelProperty(value = "对应表id（菜单权限即为菜单id）")
    private Long objectId;

    @ApiModelProperty(value = "菜单的父id")
    private Long parent;

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }
}