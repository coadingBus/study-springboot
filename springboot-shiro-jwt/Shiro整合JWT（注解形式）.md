## Springboot -Shiro整合JWT（注解形式）

在这里只展示核心代码，具体的请访问[github](https://github.com/lenyuqin/study-springboot/tree/master/springboot-shiro-jwt)

参考[timo](http://www.linln.cn/)

### 依赖导入

```xml
<dependencies>
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
    <!-- 配置 JWT -->
    <dependency>
        <groupId>com.auth0</groupId>
        <artifactId>java-jwt</artifactId>
        <version>3.10.1</version>
    </dependency>

    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.apache.shiro</groupId>
        <artifactId>shiro-spring</artifactId>
        <version>1.5.1</version>
    </dependency>
    <!-- 引入log4j2依赖 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-log4j2</artifactId>
    </dependency>


    <dependency>
        <groupId>com.github.theborakompanioni</groupId>
        <artifactId>thymeleaf-extras-shiro</artifactId>
        <version>2.0.0</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
        <exclusions>
            <exclusion>
                <groupId>org.junit.vintage</groupId>
                <artifactId>junit-vintage-engine</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
    <dependency>
        <groupId>org.aspectj</groupId>
        <artifactId>aspectjweaver</artifactId>
        <version>1.9.2</version>
        <scope>compile</scope>
    </dependency>
</dependencies>
```



日志配置[点击这里](https://blog.csdn.net/weixin_44777669/article/details/105915347)



### Jwt工具类

```java
@Slf4j
public class JwtUtil {

    /**
     * 生成JwtToken
     *
     * @param username 用户名
     * @param secret   秘钥
     * @param amount   过期天数
     */
    public static String getToken(String username, String secret, int amount) {
        User user = new User();
        user.setUsername(username);
        return getToken(user, secret, amount);
    }

    /**
     * 生成JwtToken
     *
     * @param user   用户对象
     * @param secret 秘钥
     * @param amount 过期天数
     */
    public static String getToken(User user, String secret, int amount) {
        // 过期时间
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE, amount);

        // 随机Claim
        String random = getRandomString(6);

        // 创建JwtToken对象
        String token = "";
        token = JWT.create()
                // 用户名
                .withSubject(user.getUsername())
                // 发布时间
                .withIssuedAt(new Date())
                // 过期时间
                .withExpiresAt(ca.getTime())
                // 自定义随机Claim
                .withClaim("ran", random)
                .sign(getSecret(secret, random));

        return token;
    }

    /**
     * 获取请求对象中的token数据
     */
    public static String getRequestToken(HttpServletRequest request) {
        // 获取JwtTokens失败
        String authorization = request.getHeader("authorization");
        log.info("token=========>" + authorization);
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new ResultException(JwtResultEnums.TOKEN_ERROR);
        }
        //因为有前缀，所以要去掉前缀
        return authorization.substring(7);
    }

    /**
     * 获取当前token中的用户名
     */
    public static String getSubject() {
        HttpServletRequest request = getRequest();
        String token = getRequestToken(request);
        return JWT.decode(token).getSubject();
    }

    /**
     * 验证JwtToken
     *
     * @param token JwtToken数据
     * @return true 验证通过
     * @throws TokenExpiredException    Token过期
     * @throws JWTVerificationException 令牌无效（验证不通过）
     */
    public static void verifyToken(String token, String secret) throws JWTVerificationException {
        String ran = JWT.decode(token).getClaim("ran").asString();
        log.info("验证JwtToken");
        JWTVerifier jwtVerifier = JWT.require(getSecret(secret, ran)).build();
        jwtVerifier.verify(token);
    }

    /**
     * 生成Secret混淆数据
     */
    private static Algorithm getSecret(String secret, String random) {
        String salt = "君不见黄河之水天上来，奔流到海不复回。君不见高堂明镜悲白发，朝如青丝暮成雪。";
        //String salt = "元嘉草草，封狼居胥，赢得仓皇北顾。四十三年，望中犹记，烽火扬州路。可堪回首，佛狸祠下，一片神鸦社鼓。凭谁问、廉颇老矣，尚能饭否？";
        //String salt = "安能摧眉折腰事权贵，使我不得开心颜。";
        //String salt = "大江东去，浪淘尽，千古风流人物。故垒西边，人道是，三国周郎赤壁。乱石穿空，惊涛拍岸，卷起千堆雪。江山如画，一时多少豪杰。";
        return Algorithm.HMAC256(secret + salt + "(ノ￣▽￣)ノ 皮一下" + random);
    }

    /**
     * 获取随机位数的字符串
     *
     * @param length 随机位数
     */
    public static String getRandomString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            // 获取ascii码中的字符 数字48-57 小写65-90 大写97-122
            int range = random.nextInt(75) + 48;
            range = range < 97 ? (range < 65 ? (range > 57 ? 114 - range : range) : (range > 90 ? 180 - range : range)) : range;
            sb.append((char) range);
        }
        return sb.toString();
    }

}

```



```java
/**
 * 获取HttpServlet子对象
 */
public class HttpServletUtil {

    /**
     * 获取ServletRequestAttributes对象
     */
    public static ServletRequestAttributes getServletRequest(){
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    /**
     * 获取HttpServletRequest对象
     */
    public static HttpServletRequest getRequest(){
        return getServletRequest().getRequest();
    }

    /**
     * 获取HttpServletResponse对象
     */
    public static HttpServletResponse getResponse(){
        return getServletRequest().getResponse();
    }

    /**
     * 获取请求参数
     */
    public static String getParameter(String param){
        return getRequest().getParameter(param);
    }

    /**
     * 获取请求参数，带默认值
     */
    public static String getParameter(String param, String defaultValue){
        String parameter = getRequest().getParameter(param);
        return StringUtils.isEmpty(parameter) ? defaultValue : parameter;
    }

    /**
     * 获取请求参数转换为int类型
     */
    public static Integer getParameterInt(String param){
        return Integer.valueOf(getRequest().getParameter(param));
    }

    /**
     * 获取请求参数转换为int类型，带默认值
     */
    public static Integer getParameterInt(String param, Integer defaultValue){
        return Integer.valueOf(getParameter(param, String.valueOf(defaultValue)));
    }
}
```



### 自定义注解

```java
/**
 * jwt权限注解（需要权限）
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface JwtPermissions {
}
```





```java
/**
 * 忽略jwt权限验证注解（只在拦截的地址内有效 /api/**）
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface IgnorePermissions {
}
```





### 通过AOP控制横切，控制访问

```java
/**
 * Jwt权限注解AOP
 * 通过注解拦截，只需要在需要拦截的接口上添加@JwtPermissions即可
 */
@Aspect
@Component
// @ConditionalOnProperty读取yml配置文件里面的字段值
@ConditionalOnProperty(name = "project.jwt.pattern-anno", havingValue = "true", matchIfMissing = true)
public class JwtPermissionsAop {

    @Autowired
    private JwtProjectProperties jwtProperties;

    @Autowired
    private HttpServletRequest request;

    @Pointcut("@annotation(com.site.jwt.annotation.JwtPermissions)")
    public void jwtPermissions() {
    }

    @Around("jwtPermissions()")
    public Object doPermission(ProceedingJoinPoint point) throws Throwable {

        // 获取请求对象头部token数据
        String token = JwtUtil.getRequestToken(request);


        // 验证token数据是否正确
        try {
            JwtUtil.verifyToken(token, jwtProperties.getSecret());
        } catch (TokenExpiredException e) {
            throw new ResultException(JwtResultEnums.TOKEN_EXPIRED);
        } catch (JWTVerificationException e) {
            throw new ResultException(JwtResultEnums.TOKEN_ERROR);
        }

        return point.proceed();
    }
}
```



```java
/**
 * jwt权限拦截器
 *
 */
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProjectProperties jwtProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 判断请求映射的方式是否忽略权限验证
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(IgnorePermissions.class)) {
            return true;
        }

        // 获取请求对象头部token数据
        String token = JwtUtil.getRequestToken(request);

        // 验证token数据是否正确
        try {
            JwtUtil.verifyToken(token, jwtProperties.getSecret());
        } catch (TokenExpiredException e) {
            throw new ResultException(JwtResultEnums.TOKEN_EXPIRED);
        } catch (JWTVerificationException e) {
            throw new ResultException(JwtResultEnums.TOKEN_ERROR);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
```







### Shiro拦截器

```java
/**
 * 处理session超时问题拦截器
 */
@Slf4j
public class UserAuthFilter extends AccessControlFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

        log.info("=========isAccessAllowed=====");
        if (isLoginRequest(request, response)) {
            return true;
        } else {
            Subject subject = getSubject(request, response);
            //游客，未登录 false
            return subject.getPrincipal() != null && (subject.isRemembered() || subject.isAuthenticated());
        }
    }


    /**
     * 请求头为空，那么就会重定向到登陆
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        HttpServletResponse httpResponse = WebUtils.toHttp(response);

        log.info("=========onAccessDenied======");

        if (httpRequest.getHeader("X-Requested-With") != null
                && "XMLHttpRequest".equalsIgnoreCase(httpRequest.getHeader("X-Requested-With"))) {
            httpResponse.sendError(HttpStatus.UNAUTHORIZED.value());
        } else {
            redirectToLogin(request, response);
        }
        return false;
    }
}
```





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
     * 限定这个 Realm 只处理 UsernamePasswordToken
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    /**
     * 查询数据库，将获取到的用户安全数据封装返回
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        log.warn("-------CustomRealm身份认证方法--------");
        // 从 AuthenticationToken 中获取当前用户
        String username = (String) token.getPrincipal();
        log.info("username======>"+username);
        String pwd = map.get(username);
        // 用户不存在
        if (pwd == null) {
            throw new UnknownAccountException("用户不存在！");
        }

        // 使用用户名作为盐值
        ByteSource credentialsSalt = ByteSource.Util.bytes(username + "salt");

        /**
         * 将获取到的用户数据封装成 AuthenticationInfo 对象返回，此处封装为 SimpleAuthenticationInfo 对象。
         *  参数1. 认证的实体信息，可以是从数据库中获取到的用户实体类对象或者用户名
         *  参数2. 查询获取到的登录密码
         *  参数3. 盐值
         *  参数4. 当前 Realm 对象的名称，直接调用父类的 getName() 方法即可
         */
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(username, pwd, credentialsSalt,
                getName());
        return info;
    }

    /**
     * 查询数据库，将获取到的用户的角色及权限信息返回
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
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
        return info;
    }

}
```



```java
/**
 * config配置过程
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

        /*
         * 添加自定义拦截器，重写user认证方式，处理session超时问题
         */
        HashMap<String, Filter> myFilters = new HashMap<>(16);
        myFilters.put("userAuth", new UserAuthFilter());
        shiroFilterFactoryBean.setFilters(myFilters);

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
        filterChainDefinitionMap.put("/qinjiang/**", "anon");
        //filterChainDefinitionMap.put("/level1/**", "authc");
        //filterChainDefinitionMap.put("/level2/**", "authc");
        //filterChainDefinitionMap.put("/level3/**", "authc");
        //主要这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截 剩余的都需要认证
        filterChainDefinitionMap.put("/**", "userAuth");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        // 添加自己的过滤器并且取名为jwt
        return shiroFilterFactoryBean;
    }

    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 2.Realm
        securityManager.setRealm(customRealm());
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



### 实体类对象

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String remember;
    private String username;
    private String password;
}
```



前端主要是要带着请求头发出对应的请求，要把token储存起来

这里用了封装好的ajax请求工具详情[点击这里](https://blog.csdn.net/weixin_44777669/article/details/109291288)

### 登陆请求

```js
<script type="text/javascript">

    function login() {
        // console.log(getFormData(form1))
        CoreUtil.sendAjax("/login", JSON.stringify(getFormData(form1)), function (res) {
            console.log(res.data);
            CoreUtil.setData("access_token", res.data.access_token);
            CoreUtil.setData("refresh_token", res.data.access_token);
            window.location.href = "/";
        })


    };


    //jquery 获取form表单数据通用方法
    function getFormData(formId){
        var data = {};
        var results = $(formId).serializeArray();
        $.each(results,function(index,item){
            //文本表单的值不为空才处理
            if(item.value && $.trim(item.value)!=""){
                if(!data[item.name]){
                    data[item.name]=item.value;
                }else{
                    //name属性相同的表单，值以英文,拼接
                    data[item.name]=data[item.name]+','+item.value;
                }
            }
        });
        //console.log(data);
        return data;
    }
</script>
```



### 页面内容请求

```js
<script>

    // 加载页面内容
    $(function () {
        CoreUtil.sendAjax("/home",null, function (res) {
            console.log(res.data);
            $("h3").text(res.data);
        },"GET",false)
    });

</script>
```







### 测试



#### 拦截请求路径，并根据返回数据更新html页面

API接口

```java
@JwtPermissions
@GetMapping("/home")
@ResponseBody
public ResultVo home() {
    log.info("这是JWT请求测试");
    return ResultVoUtil.success("请求成功", "你好啊！！！！！，这是JWT接口");
}
```





![image-20201031160134749](https://gitee.com/lee_ming_kang/images/raw/master/img/image-20201031160134749.png)





#### 查看日志可知，拦截有效

![image-20201031160402692](https://gitee.com/lee_ming_kang/images/raw/master/img/image-20201031160402692.png)



后续只需要在要拦截的接口方法上面加上`@JwtPermissions`就可以进行拦截了（即请求头要携带token才能访问接口），当然也可以通过配置文件进行配置拦截路径，文件是`JwtInterceptorConfig`，通过`JwtInterceptorConfig`文件配置了拦截路径，而当中有些接口不需要拦截，在接口方法上面添加`@IgnorePermissions`注解就可以了



项目github地址是：https://github.com/lenyuqin/study-springboot/tree/master/springboot-shiro-jwt