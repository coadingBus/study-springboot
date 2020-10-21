package com.site.dome.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.site.dome.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * (User)表数据库访问层
 *
 * @author lmk
 * @since 2020-10-20 20:56:19
 */
@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {


}