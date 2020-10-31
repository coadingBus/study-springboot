package com.site.jwt.interceptor;

import com.site.jwt.interceptor.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * jwt权限配置拦截器,这里可以进行路径拦截
 *
 */
@Configuration
@ConditionalOnProperty(name = "project.jwt.pattern-path", havingValue = "true")//这个注解相当于读取配置文件
public class JwtInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加的拦截地址
        //registry.addInterceptor(authenticationInterceptor)
        //        .addPathPatterns("/level1/**")
        //        .addPathPatterns("/level2/**")
        //        .addPathPatterns("/level3/**");
    }
}
