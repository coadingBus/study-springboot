package com.site.jwt.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.site.jwt.entity.User;
import com.site.jwt.entity.UserEntity;
import com.site.jwt.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;


@Slf4j
public class BaseRealm extends AuthorizingRealm {

    @Resource
    private UserService userService;
    /**
     * 限定这个 Realm 只处理我们自定义的 JwtToken
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 此处的 SimpleAuthenticationInfo 可返回任意值，密码校验时不会用到它
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
            throws AuthenticationException {
        log.info("-------JwtRealm身份认证方法--------");
        JwtToken jwtToken = (JwtToken) authcToken;
        if (jwtToken.getPrincipal() == null) {
            throw new AccountException("JWT token参数异常！");
        }
        // 从 JwtToken 中获取当前用户
        String username = jwtToken.getPrincipal().toString();
        // 查询数据库获取用户信息，
        User user = userService.getOne(new QueryWrapper<User>().lambda().eq(User::getUserName,username));

        // 用户不存在
        if (user == null) {
            throw new UnknownAccountException("用户不存在！");
        }

        //// 用户被锁定
        //if (user.getLocked()) {
        //    throw new LockedAccountException("该用户已被锁定,暂时无法登录！");
        //}
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, username, getName());
        return info;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("-------JwtRealm身份授权方法--------");
        // 获取当前用户
        User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        log.info("currentUser==========>"+currentUser);
        // UserEntity currentUser = (UserEntity) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> stringSet = new HashSet<>();
        stringSet.add("user:show");
        stringSet.add("user:admin");
        info.setStringPermissions(stringSet);
        return info;
    }
}
