package com.site.jwt.controller;

import com.site.jwt.entity.User;
import com.site.jwt.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * 客户端用户表(User)表控制层
 *
 * @author lmk
 * @since 2020-10-24 14:31:37
 */
@Api(tags = "客户端用户表(User)")
@Controller
@RequestMapping("user")
public class UserController {
    /**
     * 服务对象
     */
    @Resource
    private UserService userService;


}