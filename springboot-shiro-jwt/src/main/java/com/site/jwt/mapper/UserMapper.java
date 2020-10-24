package com.site.jwt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.site.jwt.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 客户端用户表(User)表数据库访问层
 *
 * @author lmk
 * @since 2020-10-24 14:31:35
 */
@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {


}