package com.lmk.redis2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmk.redis2.mapper.StudyMapper;
import com.lmk.redis2.entity.User;
import com.lmk.redis2.service.StudyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * (Study)表服务实现类
 *
 * @author lmk
 * @since 2020-09-10 14:44:46
 */
@Slf4j
@Service("studyService")
public class StudyServiceImpl extends ServiceImpl<StudyMapper, User> implements StudyService {

    @Resource
    private StudyMapper studyMapper;


    /**
     * 修改用户
     *
     * @param user 用户对象
     * @return 操作结果
     */
    @CacheEvict(key = "#user.id")
    @Override
    public User Update(User user) {
        log.info("保存用户【user】= "+user.toString());
        studyMapper.updateById(user);
        return user;
    }

    /**
     * 获取用户
     *
     * @param id key值
     * @return 返回结果
     */
    @Cacheable(value = "user", key = "#id")
    @Override
    public User get(Long id) {
        // 我们假设从数据库读取
        log.info("查询用户【id】= "+id);
        return studyMapper.selectById(id);

    }

    /**
     * 删除
     *
     * @param id key值
     */
    @CacheEvict(value = "user", key = "#id")
    @Override
    public void delete(Long id) {

        studyMapper.deleteById(id);
        log.info("删除用户【id】= {}", id);
    }
}