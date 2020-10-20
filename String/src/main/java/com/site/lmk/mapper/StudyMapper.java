package com.site.lmk.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.site.lmk.entity.Study;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * (Study)表数据库访问层
 *
 * @author lmk
 * @since 2020-09-25 14:35:43
 */
@Mapper
@Repository
public interface StudyMapper extends BaseMapper<Study> {


}