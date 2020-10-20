package com.lmk.redis2.controller;


import com.lmk.redis2.entity.User;
import com.lmk.redis2.service.StudyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

/**
 * (Study)表控制层
 *
 * @author lmk
 * @since 2020-09-12 17:02:19
 */
@Slf4j
@RestController
@RequestMapping("study")
public class StudyController {
    /**
     * 服务对象
     */
    @Resource
    private StudyService studyService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne/{id}")
    public ResponseEntity<User> selectOne(@PathVariable("id") Long id) {
        log.info("查询到数据==============>");
        return ResponseEntity.status(HttpStatus.OK).body(studyService.getById(id));
    }

}