package com.bon.common.shiro;

import com.bon.common.util.MyLog;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.realm.Realm;

import java.util.Collection;

/**
 * @program: base
 * @description: 重写模块化用户验证器, 根据登录界面传递的loginType参数, 获取唯一匹配的realm
 * @author: Bon
 * @create: 2018-09-10 17:13
 **/
public class ShiroModularRealmAuthenticator extends ModularRealmAuthenticator {
    private static final MyLog log = MyLog.getLog(ShiroModularRealmAuthenticator.class);


    @Override
    protected AuthenticationInfo doMultiRealmAuthentication(Collection<Realm> realms, AuthenticationToken token) throws AuthenticationException {
        Realm uniqueRealm = getUniqueRealm(realms, token);
        if (uniqueRealm == null) {
            throw new UnsupportedTokenException("没有匹配类型的realm");
        }
        return uniqueRealm.getAuthenticationInfo(token);
    }

    /**
     * 判断realms是否匹配,并返回唯一的可用的realm,否则返回空
     *
     * @param realms realm集合
     * @param token  登陆信息
     * @return 返回唯一的可用的realm
     */
    private Realm getUniqueRealm(Collection<Realm> realms, AuthenticationToken token) {
        for (Realm realm : realms) {
            if (realm.supports(token)) {
                return realm;
            }
        }
        log.error("一个可用的realm都没有找到......");
        return null;
    }

}
