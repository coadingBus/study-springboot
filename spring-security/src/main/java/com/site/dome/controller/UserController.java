package com.site.dome.controller;

import com.site.dome.entity.User;
import com.site.dome.service.UserService;;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * (User)表控制层
 *
 * @author lmk
 * @since 2020-10-20 20:56:27
 */

@Controller
@RequestMapping("user")
public class UserController {
    /**
     * 服务对象
     */
    @Resource
    private UserService userService;



}