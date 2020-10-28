package com.site.jwt.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Programmer Li
 * <p>
 * <p>
 * config配置过程
 * ShiroFilterFactoryBean
 * <p>
 * defaultWebSecurityManager
 * <p>
 * realm 对象的创建 （自定义）
 */
@Configuration
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setUnauthorizedUrl("/notRole");

        // 添加 jwt 专用过滤器，拦截除 /login 和 /logout 外的请求
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("jwtFilter", jwtFilter());
        shiroFilterFactoryBean.setFilters(filterMap);

        //添加过滤器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        // 过滤规则
        // authc:所有url都必须认证通过才可以访问;
        // anon:所有url都都可以匿名访问;
        // user: 必须拥有记住我功能才能用;
        // perms：拥有对某个资源的权限才能访问;
        // roles：拥有某个角色权限才能访问
        filterChainDefinitionMap.put("/toLogin", "anon");
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/level1/**", "jwtFilter,authc");
        filterChainDefinitionMap.put("/level2/**", "jwtFilter,authc");
        filterChainDefinitionMap.put("/level3/**", "jwtFilter,authc");
        //主要这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截 剩余的都需要认证
        filterChainDefinitionMap.put("/**", "anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        // 添加自己的过滤器并且取名为jwt
        return shiroFilterFactoryBean;
    }

    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 2.Realm
        List<Realm> realms = new ArrayList<Realm>(16);
        realms.add(baseRealm());
        realms.add(customRealm());

        securityManager.setRealms(realms);
        /*
         * 关闭shiro自带的session，详情见文档,整合springboot就把下面注释掉
         * http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
         */
        //DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        //DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        //defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        //subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        //securityManager.setSubjectDAO(subjectDAO);
        return securityManager;

    }


    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter();
    }

    /**
     * 配置 ModularRealmAuthenticator
     */
    @Bean
    public ModularRealmAuthenticator authenticator() {
        ModularRealmAuthenticator authenticator = new MultiRealmAuthenticator();
        // 设置多 Realm的认证策略，默认 AtLeastOneSuccessfulStrategy
        AuthenticationStrategy strategy = new FirstSuccessfulStrategy();
        authenticator.setAuthenticationStrategy(strategy);
        return authenticator;
    }

    /**
     * BaseRealm 配置，需实现 Realm 接口
     */
    @Bean
    BaseRealm baseRealm() {
        BaseRealm baseRealm = new BaseRealm();
        // 设置加密算法
        CredentialsMatcher credentialsMatcher = new JwtCredentialsMatcher();
        // 设置加密次数
        baseRealm.setCredentialsMatcher(credentialsMatcher);
        return baseRealm;
    }

    /**
     * CustomRealm 配置，需实现 Realm 接口
     */
    @Bean
    CustomRealm customRealm() {
        CustomRealm customRealm = new CustomRealm();
        // 设置加密算法
        customRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        customRealm.setCachingEnabled(false);
        return customRealm;
    }


    /**
     * JwtRealm 配置，需实现 Realm 接口
     */
    @Bean
    JwtRealm jwtRealm() {
        JwtRealm jwtRealm = new JwtRealm();
        // 设置加密算法
        CredentialsMatcher credentialsMatcher = new JwtCredentialsMatcher();
        // 设置加密次数
        jwtRealm.setCredentialsMatcher(credentialsMatcher);
        return jwtRealm;
    }

    /**
     * ShiroRealm 配置，需实现 Realm 接口
     */
    @Bean
    ShiroRealm shiroRealm() {
        ShiroRealm shiroRealm = new ShiroRealm();
        // 设置加密算法
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher("SHA-1");
        // 设置加密次数
        credentialsMatcher.setHashIterations(16);
        shiroRealm.setCredentialsMatcher(credentialsMatcher);
        return shiroRealm;
    }



    //@Bean
    //public CustomRealm customRealm() {
    //    CustomRealm customRealm = new CustomRealm();
    //    // 告诉realm,使用credentialsMatcher加密算法类来验证密文
    //    customRealm.setCredentialsMatcher(hashedCredentialsMatcher());
    //    customRealm.setCachingEnabled(false);
    //    return customRealm;
    //}


    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * *
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * *
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
     * * @return
     */
    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }

    //这里配置了加密算法
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        // 散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        // 散列的次数，比如散列两次，相当于 md5(md5(""));
        hashedCredentialsMatcher.setHashIterations(2);
        // storedCredentialsHexEncoded默认是true，此时用的是密码加密用的是Hex编码；false时用Base64编码
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }


    //开启shiro和thymeleaf的注解
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }


}
