package com.site.lmk.controller;


import com.site.lmk.entity.Study;
import com.site.lmk.service.StudyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.annotation.Resource;

/**
 * (Study)表控制层
 *
 * @author lmk
 * @since 2020-09-25 14:35:45
 */
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
    public Study selectOne( @PathVariable("id") String id) {
        return this.studyService.getById(id);
    }

}