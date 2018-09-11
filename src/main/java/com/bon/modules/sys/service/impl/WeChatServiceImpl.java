package com.bon.modules.sys.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.bon.common.exception.BusinessException;
import com.bon.common.shiro.ShiroToken;
import com.bon.common.util.GeneratePropertyUtil;
import com.bon.common.util.HttpClientUtils;
import com.bon.common.util.MyLog;
import com.bon.modules.sys.dao.SysUserExtendMapper;
import com.bon.modules.sys.dao.SysUserMapper;
import com.bon.modules.sys.domain.entity.SysUser;
import com.bon.modules.sys.service.WeChatService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @program: bon-bon基础项目
 * @description: 用户、角色、权限信息管理模块
 * @author: Bon
 * @create: 2018-04-27 18:00
 **/
@Service
@Transactional
public class WeChatServiceImpl implements WeChatService {

    private static final MyLog log = MyLog.getLog(WeChatServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private SysUserExtendMapper userExtendMapper;

    @Override
    public String wechatLogin(String code) {
        String appid = GeneratePropertyUtil.getProperty("appid");
        String secret = GeneratePropertyUtil.getProperty("secret");
        String url = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=%s&secret=%s&code=%s&grant_type=authorization_code",appid,secret,code);
        JSONObject result = HttpClientUtils.httpGet(url);

        String openid = result.getString("openid");
        if(null == openid){
            throw new BusinessException("微信获取用户信息失败");
        }
        String access_token = result.getString("access_token");
        String refresh_token = result.getString("refresh_token");
        SysUser user = userExtendMapper.getByUsername(openid);
        if(null == user){
            user = new SysUser();
            user.setGmtCreate(new Date());
            user.setGmtModified(new Date());
            user.setUsername(openid);
            user.setWxOpenid(openid);
            userMapper.insertSelective(user);
        }
        ShiroToken token = new ShiroToken(user.getUsername(),user.getWxOpenid(),false,"","WX");
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);
        return access_token;
    }
}
