package com.site.jwt.controller;

import com.site.jwt.annotation.IgnorePermissions;
import com.site.jwt.annotation.JwtPermissions;
import com.site.jwt.entity.User;
import com.site.jwt.result.ResultVo;
import com.site.jwt.result.ResultVoUtil;
import com.site.jwt.result.URL;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class RouterController {

    @GetMapping({"/", "/index"})
    public String index() {
        log.info("这是登陆页面 日志测试");
        return "index";
    }


    @JwtPermissions
    @GetMapping("/home")
    @ResponseBody
    public ResultVo home() {
        log.info("这是JWT请求测试");
        return ResultVoUtil.success("请求成功", "你好啊！！！！！，这是JWT接口");
    }

    @GetMapping("/level1/{id}")
    public String level1(@PathVariable("id") int id) {
        return "views/level1/" + id;
    }


    @GetMapping("/level2/{id}")
    public String level2(@PathVariable("id") int id) {
        return "views/level2/" + id;
    }

    @JwtPermissions
    @GetMapping("/level3/{id}")
    public String level3(@PathVariable("id") int id) {
        return "views/level3/" + id;
    }

}