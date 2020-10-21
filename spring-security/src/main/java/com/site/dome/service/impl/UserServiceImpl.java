package com.site.dome.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.site.dome.entity.User;
import com.site.dome.mapper.UserMapper;
import com.site.dome.service.UserService;
import org.springframework.stereotype.Service;

/**
 * (User)表服务实现类
 *
 * @author lmk
 * @since 2020-10-20 20:56:27
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}