package com.site.jwt.controller;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.site.jwt.result.BaseResponse;
import com.site.jwt.util.JwtUtils;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("test")
public class LoginController {

    @PostMapping(value = "/login")
    public Object userLogin(@RequestParam(name = "username") String userName,
                            @RequestParam(name = "password") String password, ServletResponse response) {

        log.warn("username=>"+userName+"password=>"+password);
        // 获取当前用户主体
        Subject subject = SecurityUtils.getSubject();
        String msg = null;
        boolean loginSuccess = false;
        // 将用户名和密码封装成 UsernamePasswordToken 对象
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        try {
            subject.login(token);
            msg = "登录成功。";
            loginSuccess = true;
        } catch (UnknownAccountException | IncorrectCredentialsException uae) { // 账号不存在
            msg = "用户名与密码不匹配，请检查后重新输入！";
        } // 账号与密码不匹配
        catch (LockedAccountException lae) { // 账号已被锁定
            msg = "该账户已被锁定，如需解锁请联系管理员！";
        } catch (AuthenticationException ae) { // 其他身份验证异常
            msg = "登录异常，请联系管理员！";
        }
        BaseResponse<Object> ret = new BaseResponse<Object>();
        if (loginSuccess) {
            // 若登录成功，签发 JWT token
            String jwtToken = JwtUtils.sign(userName, JwtUtils.SECRET);
            // 将签发的 JWT token 设置到 HttpServletResponse 的 Header 中
            ((HttpServletResponse) response).setHeader(JwtUtils.AUTH_HEADER, jwtToken);
            //
            ret.setCode(200);
            ret.setMsg(msg);
            return ret;
        } else {
            ret.setCode(401);
            ret.setMsg(msg);
            return ret;
        }

    }


}