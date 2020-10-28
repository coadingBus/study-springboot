package com.site.shiro.config;

import com.site.shiro.util.ShiroUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * 自定义realm
 */

@Slf4j
public class CustomRealm extends AuthorizingRealm {

    private static HashMap<String, String> map = new HashMap<>();


    //模拟数据库  密码都是123
    static {
        //根据用户名从数据库获取密码 MD5Pwd("root","123")
        // 335c38d67ad98270cd236398be193804=(lenyuqin,123)
        // c7b5a4b3d344cd1ee759b954b6a2e75d=(guest,123)
        // 4fbe67902ad1551ceaf1b971bbca64ca=(root,123)
        map.put("lenyuqin", "335c38d67ad98270cd236398be193804");
        map.put("guest", "c7b5a4b3d344cd1ee759b954b6a2e75d");
        map.put("root", "4fbe67902ad1551ceaf1b971bbca64ca");
    }


    /**
     * 这里可以注入userService,为了方便演示，我就写死了帐号了密码
     * private UserService userService;
     * 获取即将需要认证的信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("-------身份认证方法--------");
        String userName = (String) authenticationToken.getPrincipal();
        String userPwd = new String((char[]) authenticationToken.getCredentials());
        log.info("用户名==>" + userName + "========pwd===>" + userPwd);

        if (userName == null || map.get(userName) == null) {
            throw new AccountException("用户名不正确");
        } else if (!ShiroUtil.MD5Pwd(userName, userPwd).equals(map.get(userName))) {
            throw new AccountException("密码不正确");
        }
        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配,对输入的密码加密然后对数据库上的数据进行对比
        //如果想要加载自己的密码请看ShiroUtil工具类
        return new SimpleAuthenticationInfo(userName, map.get(userName), ByteSource.Util.bytes(userName + "salt"), getName());
    }

    //先认证，后授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("-------身份授权方法--------");
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        log.info("username===========>" + username);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> stringSet = new HashSet<>();
        Set<String> roleSet = new HashSet<>();

        if ("lenyuqin".equals(username)) {
            stringSet.add("user:vip1");
            stringSet.add("user:vip2");
        }
        if ("guest".equals(username)) {
            stringSet.add("user:vip2");
            stringSet.add("user:vip3");
        }
        if ("root".equals(username)) {
            stringSet.add("user:vip1");
            stringSet.add("user:vip2");
            stringSet.add("user:vip3");
        }
        info.setStringPermissions(stringSet);

        //这里还有角色的写法，我这为了简单就不添加这种写法了
        //roleSet.add("root");
        //roleSet.add("lenyuqin");
        //roleSet.add("guest");
        //info.setRoles(roleSet);
        return info;
    }


}
