package com.site.dome.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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