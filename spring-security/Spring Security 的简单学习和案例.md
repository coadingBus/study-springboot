## Springboot——Spring Security 的简单学习和案例（狂神说学习案例）

狂神学习视频地址：https://www.bilibili.com/video/BV1PE411i7CV?p=35 

Spring Security是 **一种基于 Spring AOP 和 Servlet 过滤器的安全框架**。它提供全面的安全性解决方案，同时在 Web 请求级和方法调用级处理身份确认和授权

### 一、SpringSceurity工作流程

网上找一张图，觉得画的挺好的，比较容易理解。不然换的是源码流程图很难去理解。



![img](https://gitee.com/lee_ming_kang/images/raw/master/img/1090617-20200422201332158-1598457501.png)

记住下面几个类

- AuthenticationManagerBuilder 自定义认证策略
-  WebSecurityConfigurerAdapter 自定义Security 策略
- @EnableWebSecurity 开启WebSecurity模式

Spring Security的两个主要目标就是“认证”和“授权”（访问控制）

认证：Authentication

授权：authorization



### 二、项目测试

#### 导入依赖

```xml
<!--thymeleaf -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-simple</artifactId>
    <version>1.7.26</version>
    <scope>compile</scope>
</dependency>
<!--thymeleaf和springsecurity5的整合  -->
<dependency>
    <groupId>org.thymeleaf.extras</groupId>
    <artifactId>thymeleaf-extras-springsecurity5</artifactId>
    <version>3.0.4.RELEASE</version>
</dependency>
<!-- 日志，引入log4j2依赖 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-log4j2</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-slf4j-impl</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<!-- 引入springsecurity依赖 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
    <!-- 日志依赖冲突 -->
    <exclusions>
        <exclusion>
            <artifactId>spring-boot-starter-logging</artifactId>
            <groupId>org.springframework.boot</groupId>
        </exclusion>
    </exclusions>
</dependency>
```



#### 日志文件log4j2-spring.xml

```xml
<Configuration status="INFO" monitorInterval="30">
    <Properties>
        <!--  输出路径  -->
        <Property name="logpath">./log</Property>
    </Properties>
    <Appenders>
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout
                    pattern="[%d][%-5p][%t] %m (%F:%L)%n"/>
        </Console>
        <RollingFile name="debug" fileName="${logpath}/debug/debug.log"
                     filePattern="${logpath}/debug/debug_%d{yyyy-MM-dd}-%i.log">
            <Filters>
                <ThresholdFilter level="info" onMatch="DENY" onMismatch="NEUTRAL"/>
                <ThresholdFilter level="debug" onMatch="ACCEPT" onMismatch="DENY"/>
            </Filters>
            <PatternLayout pattern="[%d][%-5p][%t] %m (%F:%L)%n"/>
            <Policies>
                <TimeBasedTriggeringPolicy interval="24" modulate="true"/>
                <SizeBasedTriggeringPolicy size="50 MB"/>\
            </Policies>
            <DefaultRolloverStrategy max="30">
                <Delete basePath="${logpath}/debug" maxDepth="1">
                    <IfFileName glob="debug_*.log"/>
                    <IfLastModified age="15d"/>
                </Delete>
            </DefaultRolloverStrategy>
        </RollingFile>
        <RollingFile name="info" fileName="${logpath}/info/info.log"
                     filePattern="${logpath}/info/info_%d{yyyy-MM-dd}-%i.log">
            <Filters>
                <ThresholdFilter level="warn" onMatch="DENY" onMismatch="NEUTRAL"/>
                <ThresholdFilter level="info" onMatch="ACCEPT" onMismatch="DENY"/>
            </Filters>
            <PatternLayout pattern="[%d][%-5p][%t] %m (%F:%L)%n"/>
            <Policies>
                <TimeBasedTriggeringPolicy interval="24" modulate="true"/>
                <SizeBasedTriggeringPolicy size="50 MB"/>\
            </Policies>
            <DefaultRolloverStrategy max="30">
                <Delete basePath="${logpath}/info" maxDepth="1">
                    <IfFileName glob="info_*.log"/>
                    <IfLastModified age="15d"/>
                </Delete>
            </DefaultRolloverStrategy>
        </RollingFile>
        <RollingFile name="warn" fileName="${logpath}/warn/warn.log"
                     filePattern="${logpath}/warn/warn_%d{yyyy-MM-dd}-%i.log">
            <Filters>
                <ThresholdFilter level="error" onMatch="DENY" onMismatch="NEUTRAL"/>
                <ThresholdFilter level="warn" onMatch="ACCEPT" onMismatch="DENY"/>
            </Filters>
            <PatternLayout pattern="[%d][%-5p][%t] %m (%F:%L)%n"/>
            <Policies>
                <TimeBasedTriggeringPolicy interval="24" modulate="true"/>
                <SizeBasedTriggeringPolicy size="50 MB"/>\
            </Policies>
            <DefaultRolloverStrategy max="30">
                <Delete basePath="${logpath}/warn" maxDepth="1">
                    <IfFileName glob="warn_*.log"/>
                    <IfLastModified age="15d"/>
                </Delete>
            </DefaultRolloverStrategy>
        </RollingFile>
        <RollingFile name="error" fileName="${logpath}/error/error.log"
                     filePattern="${logpath}/error/error_%d{yyyy-MM-dd}-%i.log">
            <Filters>
                <ThresholdFilter level="fatal" onMatch="DENY" onMismatch="NEUTRAL"/>
                <ThresholdFilter level="error" onMatch="ACCEPT" onMismatch="DENY"/>
            </Filters>
            <PatternLayout pattern="[%d][%-5p][%t] %m (%F:%L)%n"/>
            <Policies>
                <TimeBasedTriggeringPolicy interval="24" modulate="true"/>
                <!--   每个文件最大50M -->
                <SizeBasedTriggeringPolicy size="50 MB"/>\
            </Policies>
            <DefaultRolloverStrategy max="30">
                <Delete basePath="${logpath}/error" maxDepth="1">
                    <IfFileName glob="error_*.log"/>
                    <!-- 设置最大保存时间为15天-->
                    <IfLastModified age="15d"/>
                </Delete>
            </DefaultRolloverStrategy>
        </RollingFile>

    </Appenders>
    <!--切换输出级别-->
    <Loggers>
        <Root level="info">
            <AppenderRef ref="Console"/>
            <AppenderRef ref="debug"/>
            <AppenderRef ref="info"/>
            <AppenderRef ref="warn"/>
            <AppenderRef ref="error"/>
        </Root>
    </Loggers>
</Configuration>
```



#### SecurityConfig文件配置

```java
@Configuration
@EnableWebSecurity // 开启WebSecurity模式
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    //链式编程
    // 定制请求的授权规则
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 首页所有人可以访问
        http.authorizeRequests().antMatchers("/").permitAll()//首页所有人都可以访问
                .antMatchers("/level1/**").hasRole("vip1")
                .antMatchers("/level2/**").hasRole("vip2")
                .antMatchers("/level3/**").hasRole("vip3");
        // 开启自动配置的登录功能
        // /login 请求来到登录页
        // /login?error 重定向到这里表示登录失败 tologin 映射到login
        http.formLogin().loginPage("/tologin").loginProcessingUrl("/login")
                .usernameParameter("user") // default is username 修改默认登陆接受参数 如果表单是user 这里参数就填user
                .passwordParameter("pwd"); // default is password 修改默认登陆接受参数 如果表单是pwd 这里参数就填pwd
        //开启自动配置的注销的功能
        //注销请求 默认请求是post 地址是这个 /logout 要修改地址的话就用logoutUrl()来修改地址  invalidateHttpSession(true)这个是初始化Session
        http.logout().logoutSuccessUrl("/").deleteCookies().logoutUrl("/signout").invalidateHttpSession(true);

        //防止网站攻击，get请求不安全，因为页面里面是get请求，而注销是需要post请求所以要关闭这个，所以注销失败的原因就在这
        http.csrf().disable();
        //开启记住我的功能，默认保存14天
        http.rememberMe()
                .rememberMeParameter("remember");//自定义前端rememberMe参数 默认是remember-me
    }

    //认证，将user赋予对应的角色
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        //在内存中定义，也可以在jdbc中去拿....
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("lenyuqin").password(new BCryptPasswordEncoder().encode("123456")).roles("vip2", "vip3")
                .and()
                .withUser("root").password(new BCryptPasswordEncoder().encode("123456")).roles("vip1", "vip2", "vip3")
                .and()
                .withUser("guest").password(new BCryptPasswordEncoder().encode("123456")).roles("vip1", "vip2");


        //数据库查询
        //auth.jdbcAuthentication()
        //        .dataSource(dataSource)
        //        .withDefaultSchema()
        //        .withUser("user").password("password").roles("USER").and()
        //        .withUser("admin").password("password").roles("USER", "ADMIN");
    }
}
```

#### 路径跳转

```java
@Slf4j
@Controller
public class RouterController {

    @GetMapping({"/", "/index"})
    public String index() {
        log.info("这是登陆页面 日志测试");
        return "index";
    }

    @GetMapping("/toLogin")
    public String toLogin() {
        return "views/login";
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



**Spring Security整合thymeleaf的模板**

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/extras/spring-security">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
判断用户是否已经登陆认证，引号内的参数必须是isAuthenticated()。
sec:authorize="isAuthenticated()"

获得当前用户的用户名，引号内的参数必须是name。
sec:authentication=“name”

判断当前用户是否拥有指定的权限。引号内的参数为权限的名称。
sec:authorize=“hasRole(‘role’)”

获得当前用户的全部角色，引号内的参数必须是principal.authorities。
sec:authentication="principal.authorities"


使用sec:authorize="isAuthenticated()" 判断是否登录 登录之后显示的页面内容
<div sec:authorize="isAuthenticated()">
    <h2><span sec:authentication="name"></span>,您好 您的身份是
        <span sec:authentication="principal.authorities"></span>
    </h2>
</div>


使用sec:authorize="hasRole('VIP1')"控制
<div sec:authorize="hasRole('VIP1')">
    <h3>我的VIP角色</h3>
    <ul>
        <li><a th:href="@{/level1/1}">免费阅读</a></li>
        <li><a th:href="@{/level1/2}">购买8折</a></li>
    </ul>
</div>
</body>
</html>
```

至于前端页面可以去我[github网站](https://github.com/lenyuqin/study-springboot/tree/master/spring-security)上下载，下面是进行测试

目前定义了3个角色lenyuqin，root，guest，分别拥有不同的权限，按照定义，不同的角色是可以访问不同的页面的，下面进行登陆验证（密码均为123456）

**首页**

![image-20201028165837054](https://gitee.com/lee_ming_kang/images/raw/master/img/image-20201028165837054.png)

**登陆页面**

![image-20201028170014277](https://gitee.com/lee_ming_kang/images/raw/master/img/image-20201028170014277.png)

**root用户**



![image-20201028170040801](https://gitee.com/lee_ming_kang/images/raw/master/img/image-20201028170040801.png)





**guest用户**

![image-20201028170155240](https://gitee.com/lee_ming_kang/images/raw/master/img/image-20201028170155240.png)



**lenyuqin用户**

![image-20201028170339996](https://gitee.com/lee_ming_kang/images/raw/master/img/image-20201028170339996.png)





配置可以从源码上面的例子参考

```java
   /**
     * 通过 {@link #authenticationManager()} 方法的默认实现尝试获取一个 {@link AuthenticationManager}.
     * 如果被复写, 应该使用{@link AuthenticationManagerBuilder} 来指定 {@link AuthenticationManager}.
     *
     * 例如, 可以使用以下配置在内存中进行注册公开内存的身份验证{@link UserDetailsService}:
     *
     * // 在内存中添加 user 和 admin 用户
     * @Override
     * protected void configure(AuthenticationManagerBuilder auth) {
     *     auth
     *       .inMemoryAuthentication().withUser("user").password("password").roles("USER").and()
     *         .withUser("admin").password("password").roles("USER", "ADMIN");
     * }
     *
     * // 将 UserDetailsService 显示为 Bean
     * @Bean
     * @Override
     * public UserDetailsService userDetailsServiceBean() throws Exception {
     *     return super.userDetailsServiceBean();
     * }
     *
     */
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        this.disableLocalConfigureAuthenticationBldr = true;
    }
 
 
    /**
     * 复写这个方法来配置 {@link HttpSecurity}. 
     * 通常，子类不能通过调用 super 来调用此方法，因为它可能会覆盖其配置。 默认配置为：
     * 
     * http.authorizeRequests().anyRequest().authenticated().and().formLogin().and().httpBasic();
     *
     */
    protected void configure(HttpSecurity http) throws Exception {
        logger.debug("Using default configure(HttpSecurity). If subclassed this will potentially override subclass configure(HttpSecurity).");
​
        http
            .authorizeRequests()
                .anyRequest().authenticated()
                .and()
            .formLogin().and()
            .httpBasic();
    }
```

github地址：https://github.com/lenyuqin/study-springboot/tree/master/spring-security





**`每天进步一点点，一个努力改变自己的小白`**