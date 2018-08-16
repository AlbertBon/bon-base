package com.bon.modules.sys.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @program: 后台-基础项目
 * @description: 获取权限参数类
 * @author: Bon
 * @create: 2018-08-07 11:01
 **/
@ApiModel("获取权限参数类")
public class PermissionGetDTO {

    @ApiModelProperty(value = "权限id")
    private Long permissionId;

    @ApiModelProperty(value = "00:菜单权限")
    private String type;

    @ApiModelProperty(value = "对应表id（菜单权限即为菜单id）")
    private Long objectId;

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
