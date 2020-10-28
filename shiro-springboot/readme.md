## springboot shiro整合

从狂神说总结的小案例

### 一、shiro简介







![img](https://gitee.com/lee_ming_kang/images/raw/master/img/15087669-df91fa9f6c3e40d7.png)










shiro主要有三大功能模块：

1. Subject：主体，一般指用户。
2. SecurityManager：安全管理器，管理所有Subject，可以配合内部安全组件。(类似于SpringMVC中的DispatcherServlet)
3. Realms：用于进行权限信息的验证，一般需要自己实现。

细分功能

1. Authentication：身份认证/登录(账号密码验证)。
2. Authorization：授权，即角色或者权限验证。
3. Session Manager：会话管理，用户登录后的session相关管理。
4. Cryptography：加密，密码加密等。
5. Web Support：Web支持，集成Web环境。
6. Caching：缓存，用户信息、角色、权限等缓存到如redis等缓存中。
7. Concurrency：多线程并发验证，在一个线程中开启另一个线程，可以把权限自动传播过去。
8. Testing：测试支持；
9. Run As：允许一个用户假装为另一个用户（如果他们允许）的身份进行访问。
10. Remember Me：记住我，登录后，下次再来的话不用登录了。



Authentication（认证）, Authorization（授权）, Session Management（会话管理）, Cryptography（加密）代表Shiro应用安全的四大基石。

它们分别是：

- Authentication（认证）：用户身份识别，通常被称为用户“登录”。
- Authorization（授权）：访问控制。比如某个用户是否具有某个操作的使用权限。
- Session Management（会话管理）：特定于用户的会话管理,甚至在非web 应用程序。
- Cryptography（加密）：在对数据源使用加密算法加密的同时，保证易于使用。

除此之外，还有其他的功能来支持和加强这些不同应用环境下安全领域的关注点。

特别是对以下的功能支持：

- Web支持：Shiro 提供的 web 支持 api ，可以很轻松的保护 web 应用程序的安全。
- 缓存：缓存是 Apache Shiro 保证安全操作快速、高效的重要手段。
- 并发：Apache Shiro 支持多线程应用程序的并发特性。
- 测试：支持单元测试和集成测试，确保代码和预想的一样安全。
- “Run As”：这个功能允许用户在许可的前提下假设另一个用户的身份。
- “Remember Me”：跨 session 记录用户的身份，只有在强制需要时才需要登录。



![img](https://gitee.com/lee_ming_kang/images/raw/master/img/616891-20190620164858960-1788423976.png)

 主要流程

在概念层，Shiro 架构包含三个主要的理念：Subject, SecurityManager 和 Realm。下面的图展示了这些组件如何相互作用，我们将在下面依次对其进行描述。





![img](https://gitee.com/lee_ming_kang/images/raw/master/img/616891-20190620165638338-1245159826.png)









三个主要理念：

- Subject：代表当前用户，Subject 可以是一个人，也可以是第三方服务、守护进程帐户、时钟守护任务或者其它当前和软件交互的任何事件。
- SecurityManager：管理所有Subject，SecurityManager 是 Shiro 架构的核心，配合内部安全组件共同组成安全伞。
- Realms：用于进行权限信息的验证，我们自己实现。Realm 本质上是一个特定的安全 DAO：它封装与数据源连接的细节，得到Shiro 所需的相关的数据。在配置 Shiro 的时候，你必须指定至少一个Realm 来实现认证（authentication）和/或授权（authorization）。

我们需要实现Realms的Authentication 和 Authorization。其中 Authentication 是用来验证用户身份，Authorization 是授权访问控制，用于对用户进行的操作授权，证明该用户是否允许进行当前操作，如访问某个链接，某个资源文件等。



#### 一些常见的shiro异常用于捕获：

##### 1. AuthenticationException 认证异常

Shiro在登录认证过程中，认证失败需要抛出的异常。 AuthenticationException包含以下子类：

###### 1.1. CredentitalsException 凭证异常

IncorrectCredentialsException             不正确的凭证
 ExpiredCredentialsException               凭证过期

###### 1.2. AccountException 账号异常

- ConcurrentAccessException:      并发访问异常（多个用户同时登录时抛出）
- UnknownAccountException:        未知的账号
- ExcessiveAttemptsException:     认证次数超过限制
- DisabledAccountException:       禁用的账号
- LockedAccountException:     账号被锁定
- UnsupportedTokenException:      使用了不支持的Token

##### 2. AuthorizationException: 授权异常

Shiro在登录认证过程中，授权失败需要抛出的异常。 AuthorizationException包含以下子类：

###### 2.1.  UnauthorizedException:

抛出以指示请求的操作或对请求的资源的访问是不允许的。

###### 2.2. UnanthenticatedException:

当尚未完成成功认证时，尝试执行授权操作时引发异常。



前面介绍写的比较乱，都是从网上找的，自己对于shiro理解还不是很深刻，暂时只会简单的运用，所以概念性的知识我整理不好（请谅解(╥╯^╰╥)）

### 二、项目测试

#### 导入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
    <exclusions>
        <exclusion>
            <artifactId>spring-boot-starter-logging</artifactId>
            <groupId>org.springframework.boot</groupId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
<!-- 引入log4j2依赖 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-log4j2</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-spring</artifactId>
    <version>1.5.1</version>
</dependency>

<!-- shiro整合thymeleaf依赖 -->
<dependency>
    <groupId>com.github.theborakompanioni</groupId>
    <artifactId>thymeleaf-extras-shiro</artifactId>
    <version>2.0.0</version>
</dependency>
```





#### 日志文件log4j2-spring.xml的配置

[点击这里](https://blog.csdn.net/weixin_44777669/article/details/105915347)



#### ShiroConfig

```java
@Configuration
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setUnauthorizedUrl("/notRole");
        shiroFilterFactoryBean.setSuccessUrl("/index");

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
        filterChainDefinitionMap.put("/front/**", "anon");
        filterChainDefinitionMap.put("/api/**", "anon");
        filterChainDefinitionMap.put("/admin/**", "authc");
        filterChainDefinitionMap.put("/views/**", "authc");
        //主要这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截 剩余的都需要认证
        filterChainDefinitionMap.put("/**", "anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        // 添加自己的过滤器并且取名为jwt
        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(customRealm());
        return securityManager;
    }

    @Bean
    public CustomRealm customRealm() {
        CustomRealm customRealm = new CustomRealm();
        // 告诉realm,使用credentialsMatcher加密算法类来验证密文
        customRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        //这里是关闭了缓存
        customRealm.setCachingEnabled(false);
        return customRealm;
    }

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

```





目前定义了3个角色lenyuqin，root，guest，分别拥有不同的权限，按照定义，不同的角色是可以访问不同的页面的，下面进行登陆验证（密码均为123）

#### 自定义realm配置

```java
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

```



#### 登陆注销请求

```java
@Slf4j
@Controller
public class HomeIndexController {
    @GetMapping(value = "/login")
    public String defaultLogin() {
        return "/login";
    }

    @PostMapping(value = "/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("remember") String remember) {
        // 从SecurityUtils里边创建一个 subject
        Subject subject = SecurityUtils.getSubject();

        // 在认证提交前准备 token（令牌）
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        // 执行认证登陆
        //账户注册的时候要调用MD5Pwd()这个方法，将密码加密，然后后面验证才能通过

        if ("1".equals(remember)) {
            subject.isRemembered();
        }
        try {
            subject.login(token);
        } catch (UnknownAccountException uae) {
            log.error("未知账户");
            return "login";
        } catch (IncorrectCredentialsException ice) {
            log.error("密码不正确");
                return "login";
        } catch (LockedAccountException lae) {
            log.error("账户已锁定");
            return "login";
        } catch (ExcessiveAttemptsException eae) {
            log.error("用户名或密码错误次数过多");
            return "login";
        } catch (AuthenticationException ae) {
            log.error("用户名或密码不正确！");
            return "login";
        }
        if (subject.isAuthenticated()) {
            return "redirect:/";
        } else {
            token.clear();
            log.error("登陆失败");
            return "login";
        }
    }

    @GetMapping(value = "/loginout")
    public String defaultLoginOut() {
        // 从SecurityUtils里边创建一个 subject
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/login";
    }

}

```





#### **页面跳转**

```java
@Slf4j
@Controller
public class RouterController {

    @GetMapping({"/", "/index"})
    public String index() {
        return "index";
    }



    @GetMapping("/level1/{id}")
    public String level1(@PathVariable("id") int id) {
        return "views/level1/" + id;
    }

    @GetMapping("/level2/{id}")
    public String level2(@PathVariable("id") int id) {
        return "views/level2/" + id;
    }

    @GetMapping("/level3/{id}")
    public String level3(@PathVariable("id") int id) {
        return "views/level3/" + id;
    }

}
```





#### Shiro整合Thymeleaf的模板（注意Shiro地址别导错了，不然没有提示）

```html
<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="Thymeleaf"

      xmlns:shiro="http://www.pollix.at/thymeleaf/shiro">

<head>

    <meta charset="UTF-8"/>

    <title>Insert title here</title>

</head>

<body>

<h3>index</h3>
<!-- 验证当前用户是否为“访客”，即未认证（包含未记住）的用户。 -->
<p shiro:guest="">Please <a href="login.html">login</a></p>
<!-- 认证通过或已记住的用户。 -->
<p shiro:user="">
    Welcome back John! Not John? Click <a href="login.html">here</a> to login.
</p>
<!-- 已认证通过的用户。不包含已记住的用户，这是与user标签的区别所在。 -->
<p shiro:authenticated="">
    Hello, <span shiro:principal=""></span>, how are you today?
</p>
<a shiro:authenticated="" href="updateAccount.html">Update your contact information</a>
<!-- 输出当前用户信息，通常为登录帐号信息。 -->
<p>Hello,
    <shiro:principal/>
    , how are you today?
</p>
<!-- 未认证通过用户，与authenticated标签相对应。与guest标签的区别是，该标签包含已记住用户。 -->
<p shiro:notAuthenticated="">
    Please <a href="login.html">login</a> in order to update your credit card information.
</p>
<!-- 验证当前用户是否属于该角色。 -->
<a shiro:hasRole="admin" href="admin.html">Administer the system</a><!-- 拥有该角色 -->
<!-- 与hasRole标签逻辑相反，当用户不属于该角色时验证通过。 -->
<p shiro:lacksRole="developer"><!-- 没有该角色 -->
    Sorry, you are not allowed to developer the system.
</p>
<!-- 验证当前用户是否属于以下所有角色。 -->
<p shiro:hasAllRoles="developer, 2"><!-- 角色与判断 -->
    You are a developer and a admin.
</p>
<!-- 验证当前用户是否属于以下任意一个角色。 -->
<p shiro:hasAnyRoles="admin, vip, developer,1"><!-- 角色或判断 -->
    You are a admin, vip, or developer.
</p>
<!--验证当前用户是否拥有指定权限。 -->
<a shiro:hasPermission="userInfo:add" href="createUser.html">添加用户</a><!-- 拥有权限 -->
<!-- 与hasPermission标签逻辑相反，当前用户没有制定权限时，验证通过。 -->
<p shiro:lacksPermission="userInfo:del"><!-- 没有权限 -->
    Sorry, you are not allowed to delete user accounts.
</p>
<!-- 验证当前用户是否拥有以下所有角色。 -->
<p shiro:hasAllPermissions="userInfo:view, userInfo:add"><!-- 权限与判断 -->
    You can see or add users.
</p>
<!-- 验证当前用户是否拥有以下任意一个权限。 -->
<p shiro:hasAnyPermissions="userInfo:view, userInfo:del"><!-- 权限或判断 -->
    You can see or delete users.
</p>
<a shiro:hasPermission="pp" href="createUser.html">Create a new User</a>


</body>
</html>
```



至于前端页面可以去我[github网站](https://github.com/lenyuqin/study-springboot/tree/master/shiro-springboot)上下载，下面是进行测试

#### 测试



- **首页**

![image-20201028165837054](D:\谷歌游览器\软件\Typora\Typora\note\studyNote\springboot shiro整合.assets\08a87b749279aec43113816ce5e9f995-1603896144526.png)

- **登陆页面**

![image-20201028170014277](https://gitee.com/lee_ming_kang/images/raw/master/img/07df640b7807da437fb15a3ecced95ff.png)

- **root用户**

![image-20201028224018417](https://gitee.com/lee_ming_kang/images/raw/master/img/image-20201028224018417.png)





- **lenyuqin用户**



![image-20201028224052712](https://gitee.com/lee_ming_kang/images/raw/master/img/image-20201028224052712.png)

- **guest用户**

![image-20201028224148013](https://gitee.com/lee_ming_kang/images/raw/master/img/image-20201028224148013.png)





**github地址**：https://github.com/lenyuqin/study-springboot/tree/master/shiro-springboot



**每天进步一点点，一个努力改变自己的小白**