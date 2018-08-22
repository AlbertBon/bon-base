package com.bon.modules.sys.service.impl;

import com.bon.common.config.Constants;

import com.bon.common.shiro.ShiroToken;
import com.bon.common.domain.dto.BaseDTO;
import com.bon.common.domain.enums.ExceptionType;
import com.bon.common.exception.BusinessException;
import com.bon.common.service.RedisService;
import com.bon.modules.sys.dao.SysUserMapper;
import com.bon.modules.sys.domain.dto.LoginDTO;
import com.bon.modules.sys.domain.dto.TokenDTO;
import com.bon.modules.sys.domain.entity.SysUser;
import com.bon.modules.sys.domain.vo.LoginVO;
import com.bon.modules.sys.domain.vo.MenuRouterVO;
import com.bon.modules.sys.domain.vo.TokenVO;
import com.bon.modules.sys.service.LoginService;
import com.bon.modules.sys.service.UserService;
import com.bon.common.util.BeanUtil;
import com.bon.common.util.MyLog;
import com.bon.common.util.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
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
    private SysUserMapper userMapper;

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
        //redis模式
        Object vCode=redisService.get(key);
        if(vCode==null||!vCode.toString().equalsIgnoreCase(loginDTO.getCode())){
            throw new BusinessException(ExceptionType.VALIDATE_CODE_ERROR);
        }
        //清除seesion中code
        redisService.del(key);
        //登录
        ShiroToken token = new ShiroToken(loginDTO.getUsername(), loginDTO.getPassword(),loginDTO.getCode());
        //如果用户已登录过，先退出再登录
        if(subject.getPrincipal()!=null){
            subject.logout();
        }
        subject.login(token);

        String username = subject.getPrincipals().getPrimaryPrincipal().toString();
        BaseDTO dto = new BaseDTO();
        dto.andFind(new SysUser(),"username",username);
        SysUser user = userMapper.selectOneByExample(dto.getExample());
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
            dto.andFind(new SysUser(),"wxOpenid",dto.getWxOpenid());
            SysUser user = userMapper.selectOneByExample(dto.getExample());
            if(user==null){
                throw new BusinessException(ExceptionType.USERNAME_NULL_PASSWORD_ERROR);
            }
            // 存储到 redis 并设置过期时间(默认2小时)
            redisService.set(MessageFormat.format(Constants.RedisKey.TOKEN_USERNAME_TOKEN,user.getUsername()),token);
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
        String username= "";
        if(null!=subject.getPrincipal()){
            username =  subject.getPrincipal().toString();
            subject.logout();
        }


        LOG.info("用户{}-session登录",username);
    }
}
