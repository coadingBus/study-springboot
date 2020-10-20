package com.site.lmk;

import com.site.lmk.service.StudyService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class testApplication {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
        StudyService studyService = annotationConfigApplicationContext.getBean(StudyService.class);
        studyService.getById(1);

    }
}
