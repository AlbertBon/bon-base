package com.bon.modules.sys.domain.dto;

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
 * 实体类对应的数据表为：  sys_url
 */
@ApiModel(value ="SysUrl")
public class PermissionUrlDTO implements Serializable {

    @ApiModelProperty(value = "路径名称")
    private String urlName;

    @ApiModelProperty(value = "路径地址")
    private String urlPath;

    @ApiModelProperty(value = "路径备注")
    private String urlRemark;

    private static final long serialVersionUID = 1L;

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName == null ? null : urlName.trim();
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath == null ? null : urlPath.trim();
    }

    public String getUrlRemark() {
        return urlRemark;
    }

    public void setUrlRemark(String urlRemark) {
        this.urlRemark = urlRemark == null ? null : urlRemark.trim();
    }
}