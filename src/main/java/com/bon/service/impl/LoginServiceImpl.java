package com.bon.service.impl;

import com.bon.common.config.Constants;

import com.bon.common.config.shiro.ShiroToken;
import com.bon.common.dto.BaseDTO;
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
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sound.midi.Soundbank;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @program: bon基础项目
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
    public LoginVO loginIn(LoginDTO loginDTO) {
        Subject subject = SecurityUtils.getSubject();
        String sessionId = subject.getSession().getId().toString();
        int time = (int) subject.getSession().getTimeout();
        //校验验证码
        String key= MessageFormat.format(Constants.RedisKey.LOGIN_CAPTCHA_SESSION_ID,sessionId);
        Object vCode=subject.getSession().getAttribute(key);
        if(vCode==null||!vCode.toString().equalsIgnoreCase(loginDTO.getCode())){
            throw new BusinessException(ExceptionType.VALIDATE_CODE_ERROR);
        }
        ShiroToken token = new ShiroToken(loginDTO.getUsername(), MD5Util.encode(loginDTO.getPassword()),loginDTO.getCode());
        subject.login(token);
        subject.hasRole("123");
        String username = subject.getPrincipals().getPrimaryPrincipal().toString();
        BaseDTO dto = new BaseDTO(new User());
        dto.andFind("username",username);
        User user = userMapper.selectOneByExample(dto.getExample());
        String loginToken= MessageFormat.format(Constants.RedisKey.LOGIN_USERNAME_SESSION_ID,user.getUsername(),sessionId);

        LoginVO loginVO = new LoginVO();
        BeanUtil.copyPropertys(user,loginVO);
        LOG.info("用户{}-session登录",loginVO.getUsername());
        // TODO: 2018/5/21 给登录用户添加登录信息
        loginVO.setToken(loginToken);
        //获取菜单路由
        List<MenuRouterVO> routers=userService.getMenuRouter(user.getUsername());
        loginVO.setRouters(routers);
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
    public void loginOut() {
        Subject subject = SecurityUtils.getSubject();
        if(null==subject.getPrincipal()){
            throw new BusinessException(ExceptionType.EXPIRED_ERROR);
        }
        String username =  subject.getPrincipal().toString();
        subject.logout();

        LOG.info("用户{}-session登录",username);
    }
}
