package com.bon.modules.sys.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @program: 后台
 *
 * @description: 菜单视图类
 *
 * @author: Bon
 *
 * @create: 2018-06-03 17:24
 **/
public class MenuVO implements Serializable{

    @ApiModelProperty(value = "ID")
    private Long menuId;

    @ApiModelProperty(value = "菜单名称")
    private String name;

    @ApiModelProperty(value = "权限标识")
    private String permissionFlag;

    @ApiModelProperty(value = "菜单地址")
    private String path;

    @ApiModelProperty(value = "视图文件路径")
    private String component;

    @ApiModelProperty(value = "跳转地址（如果设置为noredirect会在面包屑导航中无连接）")
    private String redirect;

    @ApiModelProperty(value = "菜单显示名称")
    private String title;

    @ApiModelProperty(value = "菜单图标")
    private String icon;

    @ApiModelProperty(value = "00:true,01:false如果设置true，会在导航中隐藏")
    private Byte hidden;

    @ApiModelProperty(value = "00:true,01:false没有子菜单也会显示在导航中")
    private Byte alwaysShow;

    @ApiModelProperty(value = "父菜单name")
    private String parentName;

    public String getPermissionFlag() {
        return permissionFlag;
    }

    public void setPermissionFlag(String permissionFlag) {
        this.permissionFlag = permissionFlag;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Byte getHidden() {
        return hidden;
    }

    public void setHidden(Byte hidden) {
        this.hidden = hidden;
    }

    public Byte getAlwaysShow() {
        return alwaysShow;
    }

    public void setAlwaysShow(Byte alwaysShow) {
        this.alwaysShow = alwaysShow;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }
}
