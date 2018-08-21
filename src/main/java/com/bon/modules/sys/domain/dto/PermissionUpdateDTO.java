package com.bon.modules.sys.domain.dto;

import com.bon.modules.sys.domain.entity.SysMenu;
import com.bon.modules.sys.domain.entity.SysUrl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @program: 后台-基础项目
 * @description: 获取权限参数类
 * @author: Bon
 * @create: 2018-08-07 15:10
 **/
@ApiModel("新增及修改权限参数类")
public class PermissionUpdateDTO {

    @ApiModelProperty(value = "权限id")
    private Long permissionId;

    @ApiModelProperty(value = "00:菜单权限；01：接口url权限")
    private String type;

    @ApiModelProperty(value = "对应表id（菜单权限即为菜单id）")
    private Long objectId;

    @ApiModelProperty(value = "父权限id（permission表中的id值）")
    private Long parentId;

    @ApiModelProperty(value = "权限标识")
    private String permissionFlag;

    @ApiModelProperty(value = "菜单")
    private SysMenu menu;

    @ApiModelProperty(value = "接口url")
    private SysUrl url;

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public SysUrl getUrl() {
        return url;
    }

    public void setUrl(SysUrl url) {
        this.url = url;
    }

    public SysMenu getMenu() {
        return menu;
    }

    public void setMenu(SysMenu menu) {
        this.menu = menu;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
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

    public String getPermissionFlag() {
        return permissionFlag;
    }

    public void setPermissionFlag(String permissionFlag) {
        this.permissionFlag = permissionFlag;
    }

}
