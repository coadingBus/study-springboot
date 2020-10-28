package com.site.shiro.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
