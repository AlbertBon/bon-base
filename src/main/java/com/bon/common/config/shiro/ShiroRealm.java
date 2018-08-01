package com.bon.common.config.shiro;

import com.bon.common.config.Constants;
import com.bon.common.enums.ExceptionType;
import com.bon.common.exception.BusinessException;
import com.bon.common.service.RedisService;
import com.bon.domain.entity.Permission;
import com.bon.domain.entity.Role;
import com.bon.domain.entity.User;
import com.bon.domain.vo.UserVO;
import com.bon.service.UserService;
import com.bon.util.MyLog;
import com.bon.util.PropertyUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

/**
 * @program: bon基础项目
 * @description:
 * @author: Bon
 * @create: 2018-07-19 09:01
 **/
public class ShiroRealm extends AuthorizingRealm {
    public static final MyLog log = MyLog.getLog(ShiroRealm.class);
    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("权限配置-->ShiroRealm.doGetAuthorizationInfo()");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
//        User user = (User) principals.getPrimaryPrincipal();
        String username = principals.getPrimaryPrincipal().toString();
        List<Role> roleList = userService.getRoleByUsername(username);
        for(Role role:roleList){
            authorizationInfo.addRole(role.getRoleFlag());
            List<Permission> permissionList = userService.getPermissionByRoleFlag(role.getRoleFlag());
            for(Permission permission:permissionList){
                authorizationInfo.addStringPermission(permission.getPermissionFlag());
            }
        }
        return authorizationInfo;
    }

    /*主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确。*/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
            throws AuthenticationException {
        log.info("身份认证-->ShiroRealm.doGetAuthenticationInfo()");
        ShiroToken token = (ShiroToken) authcToken;

        //获取用户的输入的账号.
        String username = (String) token.getPrincipal();
        //通过username从数据库中查找 User对象，如果找到，没找到.
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return null;
        }
//        if (userInfo.getState() == 1) { //账户冻结
//            throw new LockedAccountException();
//        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user.getUsername(), //用户名
                user.getPassword(), //密码
                getName()  //realm name
        );
        //设置盐
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(user.getSalt()));
        return authenticationInfo;
    }

}
