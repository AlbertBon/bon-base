package com.bon.service.impl;

import com.bon.common.config.Constants;

import com.bon.common.enums.ExceptionType;
import com.bon.common.exception.BusinessException;
import com.bon.common.service.RedisService;
import com.bon.dao.UserMapper;
import com.bon.domain.dto.LoginDTO;
import com.bon.domain.dto.TokenDTO;
import com.bon.domain.entity.User;
import com.bon.domain.vo.LoginVO;
import com.bon.domain.vo.MenuRouterVO;
import com.bon.domain.vo.TokenVO;
import com.bon.service.LoginService;
import com.bon.service.UserService;
import com.bon.util.BeanUtil;
import com.bon.util.MD5Util;
import com.bon.util.MyLog;
import com.bon.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @program: dubbo-wxmanage
 * @description: 登录模块实现类
 * @author: Bon
 * @create: 2018-05-16 16:37
 **/
@Service
public class LoginServiceImpl implements LoginService {

    private static final MyLog LOG = MyLog.getLog(LoginServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserService userService;

    @Override
    public LoginVO loginIn(LoginDTO loginDTO, String sessionId) {
        String vCode=redisService.get(MessageFormat.format(Constants.RedisKey.LOGIN_CAPTCHA_SESSION_ID,sessionId));
        if(!vCode.equalsIgnoreCase(loginDTO.getCode())){
            throw new BusinessException(ExceptionType.VALIDATE_CODE_ERROR);
        }

        loginDTO.andFind(new User(),"username",loginDTO.getUsername());
        loginDTO.andFind(new User(),"password", MD5Util.encode(loginDTO.getPassword()));
        User user = userMapper.selectOneByExample(loginDTO.getExample());
        if(user == null ){
            throw new BusinessException(ExceptionType.USERNAME_OR_PASSWORD_ERROR);
        }

        String key= MessageFormat.format(Constants.RedisKey.LOGIN_USERNAME_SESSION_ID,user.getUsername(),sessionId);
        redisService.create(key,user.getUsername()+"_"+ System.currentTimeMillis()+"_"+sessionId);

        LoginVO loginVO = new LoginVO();
        BeanUtil.copyPropertys(user,loginVO);
        LOG.info("用户{}-session登录",loginVO.getUsername());
        // TODO: 2018/5/21 给登录用户添加登录信息
        loginVO.setToken(key);
        //获取菜单路由
//        List<MenuRouterVO> routers=userService.getMenuRouterByUser(user.getUserId());
//        loginVO.setRouters(routers);
        return loginVO;
    }

    @Override
    public TokenVO getToken(TokenDTO dto) {
        // 使用 uuid 作为源 token
        String token = UUID.randomUUID().toString().replace("-", "");
        if(StringUtils.isNotBlank(dto.getWxOpenid())){
            dto.andFind(new User(),"wx_openid",dto.getWxOpenid());
            User user = userMapper.selectOneByExample(dto.getWxOpenid());
            if(user==null){
                throw new BusinessException(ExceptionType.USERNAME_NULL_PASSWORD_ERROR);
            }
            // 存储到 redis 并设置过期时间(默认2小时)
            redisService.create(MessageFormat.format(Constants.RedisKey.TOKEN_USERNAME_TOKEN,user.getUsername(),token),token);
        }
        TokenVO vo = new TokenVO();
        vo.setToken(token);
        return vo;
    }

    @Override
    public boolean check(String pattern) {
        String key = redisService.findKey(pattern);
        if (key == null) {
            return false;
        }
        redisService.expire(key, Constants.TOKEN_EXPIRES_SECONDS);
        return true;
    }

    @Override
    public void loginOut(String sessionId) {
        String username;
        String tokenPattern = redisService.findKey(MessageFormat.format(Constants.RedisKey.TOKEN_USERNAME_TOKEN,'*',sessionId));
        String sessionPattern = redisService.findKey(MessageFormat.format(Constants.RedisKey.LOGIN_USERNAME_SESSION_ID,'*',sessionId));
        if(tokenPattern!=null){
            List list = Arrays.asList(tokenPattern.split("_"));
            username = tokenPattern.split("_")[list.size()-2];
            LOG.info("用户{}-token登出",username);
        }
        if(sessionPattern!=null){
            List list = Arrays.asList(sessionPattern.split("_"));
            username = sessionPattern.split("_")[list.size()-2];
            LOG.info("用户{}-session登出",username);
        }
        redisService.removeByPattern(MessageFormat.format(Constants.RedisKey.TOKEN_USERNAME_TOKEN,'*',sessionId));
        redisService.removeByPattern(MessageFormat.format(Constants.RedisKey.LOGIN_USERNAME_SESSION_ID,'*',sessionId));
    }
}
