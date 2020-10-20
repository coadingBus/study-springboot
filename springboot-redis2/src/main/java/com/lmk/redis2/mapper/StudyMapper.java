package com.lmk.redis2.mapper;

import com.lmk.redis2.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * (Study)表数据库访问层
 *
 * @author lmk
 * @since 2020-09-10 14:44:44
 */
@Mapper
@Repository 
public interface StudyMapper extends BaseMapper<User> {


}