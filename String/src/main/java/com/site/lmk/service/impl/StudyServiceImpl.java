package com.site.lmk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.site.lmk.entity.Study;
import com.site.lmk.mapper.StudyMapper;
import com.site.lmk.service.StudyService;
import org.springframework.stereotype.Service;

/**
 * (Study)表服务实现类
 *
 * @author lmk
 * @since 2020-09-25 14:35:46
 */
@Service("studyService")
public class StudyServiceImpl extends ServiceImpl<StudyMapper, Study> implements StudyService {

}