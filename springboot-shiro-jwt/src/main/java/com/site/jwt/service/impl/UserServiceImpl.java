package com.site.jwt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.site.jwt.entity.User;
import com.site.jwt.mapper.UserMapper;
import com.site.jwt.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 客户端用户表(User)表服务实现类
 *
 * @author lmk
 * @since 2020-10-24 14:31:38
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}