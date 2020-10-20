package com.lmk.redis2.service;

import com.lmk.redis2.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * (Study)表服务实现类
 *
 * @author lmk
 * @since 2020-09-10 14:44:45
 */
public interface StudyService extends IService<User>{
    /**
     * 修改用户
     *
     * @param user 用户对象
     * @return 操作结果
     */
    User Update(User user);

    /**
     * 获取用户
     *
     * @param id key值
     * @return 返回结果
     */
    User get(Long id);

    /**
     * 删除
     *
     * @param id key值
     */
    void delete(Long id);
}