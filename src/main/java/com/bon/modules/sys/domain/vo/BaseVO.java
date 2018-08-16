package com.bon.modules.sys.domain.vo;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @program: 后台-基础项目
 * @description: 基础视图视图
 * @author: Bon
 * @create: 2018-08-07 11:07
 **/
public class BaseVO {

    private Map map;

    private JSONObject jsonObject;

    private String jsonString;

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }
}
