package com.site.jwt.controller;

import com.site.jwt.annotation.IgnorePermissions;
import com.site.jwt.annotation.JwtPermissions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
public class RouterController {

    @IgnorePermissions
    @GetMapping({"/", "/index"})
    public String index() {
        log.info("这是登陆页面 日志测试");
        return "index";
    }


    @JwtPermissions
    @GetMapping("/level1/{id}")
    public String level1(@PathVariable("id") int id) {
        return "views/level1/" + id;
    }

    @JwtPermissions
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