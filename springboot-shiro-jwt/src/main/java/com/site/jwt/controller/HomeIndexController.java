package com.site.jwt.controller;


import com.site.jwt.annotation.IgnorePermissions;
import com.site.jwt.entity.User;
import com.site.jwt.interceptor.JwtProjectProperties;
import com.site.jwt.result.ResultVo;
import com.site.jwt.result.ResultVoUtil;
import com.site.jwt.result.URL;
import com.site.jwt.util.HttpServletUtil;
import com.site.jwt.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


@Slf4j
@Controller
public class HomeIndexController {

    @Autowired
    private JwtProjectProperties properties;

    @IgnorePermissions
    @GetMapping(value = "/login")
    public String defaultLogin() {
        return "/login";
    }

    @IgnorePermissions
    @ResponseBody
    @PostMapping(value = "/login")
    public ResultVo login(@RequestBody User user) {

        log.info("=========登陆测试===========");
        // 从SecurityUtils里边创建一个 subject
        Subject subject = SecurityUtils.getSubject();
        log.info("==>" + user);

        // 在认证提交前准备 token（令牌）
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        // 执行认证登陆
        //账户注册的时候要调用MD5Pwd()这个方法，将密码加密，然后后面验证才能通过
        if ("1".equals(user.getRemember())) {
            subject.isRemembered();
        }
        try {
            subject.login(token);
        } catch (UnknownAccountException uae) {
            log.error("未知账户");
            return ResultVoUtil.error("未知账户", new URL("/login"));
        } catch (IncorrectCredentialsException ice) {
            log.error("密码不正确");
            return ResultVoUtil.error("密码不正确", new URL("/login"));
        } catch (LockedAccountException lae) {
            log.error("账户已锁定");
            return ResultVoUtil.error("账户已锁定", new URL("/login"));
        } catch (ExcessiveAttemptsException eae) {
            log.error("用户名或密码错误次数过多");
            return ResultVoUtil.error("用户名或密码错误次数过多", new URL("/login"));
        } catch (AuthenticationException ae) {
            log.error("用户名或密码不正确！");
            return ResultVoUtil.error("用户名或密码不正确", new URL("/login"));
        }
        if (subject.isAuthenticated()) {
            // 若登录成功，签发 JWT token
            String jWTToken = JwtUtil.getToken(user.getUsername(), properties.getSecret(), properties.getExpired());
            HashMap<String, String> map = new HashMap<>();

            log.info("access_token===>" + jWTToken);
            map.put("access_token", jWTToken);
            map.put("url", HttpServletUtil.getRequest().getContextPath() + "/");
            return ResultVoUtil.success("登录成功", map);
        } else {
            token.clear();
            log.error("登陆失败");
            return ResultVoUtil.error("登陆失败", new URL("/login"));
        }
    }

    @IgnorePermissions
    @GetMapping(value = "/loginout")
    public String defaultLoginOut() {
        // 从SecurityUtils里边创建一个 subject
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/login";
    }

}
