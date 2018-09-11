package com.bon.common.shiro;

import com.bon.common.service.RedisService;
import com.bon.common.util.MyLog;
import com.bon.modules.sys.domain.entity.SysUser;
import com.bon.modules.sys.service.ShiroService;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @program: bon基础项目
 * @description:
 * @author: Bon
 * @create: 2018-07-19 09:01
 **/
public class ShiroRealmWechat extends ShiroRealm {
    public static final MyLog log = MyLog.getLog(ShiroRealmWechat.class);
    @Autowired
    private ShiroService shiroService;

    @Autowired
    private RedisService redisService;

    /*主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确。*/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
            throws AuthenticationException {
        log.info("微信身份认证-->ShiroRealmWechat.doGetAuthenticationInfo()");
        ShiroToken token = (ShiroToken) authcToken;

        //获取用户的输入的账号.
        String username = (String) token.getPrincipal();
        //通过username从数据库中查找 User对象，如果找到，没找到.
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        SysUser user = shiroService.getUserByUsername(username);
        if (user == null) {
            throw new UnknownAccountException();//没找到帐号
        }
        if (user.getSalt() == "01") {
            throw new LockedAccountException(); //帐号冻结
        }

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user.getUsername(), //用户名
                user.getPassword(), //密码
                getName()  //realm name
        );
        return authenticationInfo;
    }
}
