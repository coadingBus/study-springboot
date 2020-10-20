package com.lmk.redis2.service;

import com.lmk.redis2.Redis2ApplicationTests;
import com.lmk.redis2.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * Redis - 缓存测试
 * </p>
 *
 */
@Slf4j
public class UserServiceTest extends Redis2ApplicationTests {
    @Autowired
    private StudyService studyService;

    /**
     * 获取两次，查看日志验证缓存
     */
    @Test
    public void getTwice() {
        // 模拟查询id为1的用户
        User user1 = studyService.get(5L);
        System.out.println(user1);
        //log.debug("【user1】= {}", user1);

        //// 再次查询
        //User user2 = studyService.get(1L);
        ////System.out.println(user2);
        //log.info("【user2】= {}"+user2.toString());
        //// 查看日志，只打印一次日志，证明缓存生效
    }

    /**
     * 先存，再查询，查看日志验证缓存
     */
    @Test
    public void getAfterSave() {
        //studyService.saveorUpdate(new User("li2",6L,"女","1230"));

        User user = studyService.get(6L);

        log.info("【user】= {}"+user.toString());
        // 查看日志，只打印保存用户的日志，查询是未触发查询日志，因此缓存生效
    }

    /**
     * 测试删除，查看redis是否存在缓存数据
     */
    @Test
    public void deleteUser() {
        // 查询一次，使redis中存在缓存数据
        studyService.get(1L);
        // 删除，查看redis是否存在缓存数据
        studyService.delete(1L);
    }



}
