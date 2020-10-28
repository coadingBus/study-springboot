## spring-security注销失败的原因（404页面）



```java
//注销请求 默认请求是post 地址是这个 /logout 要修改地址的话就用logoutUrl()来修改地址  invalidateHttpSession(true)这个是初始化Session
http.logout().logoutSuccessUrl("/").deleteCookies().logoutUrl("/signout").invalidateHttpSession(true);

//防止网站攻击，get请求不安全，因为页面里面是get请求，而注销是需要post请求所以要关闭这个，所以注销失败的原因就在这
http.csrf().disable();
```

