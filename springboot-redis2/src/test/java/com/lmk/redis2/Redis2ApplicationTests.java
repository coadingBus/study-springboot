package com.lmk.redis2;

import com.lmk.redis2.entity.OSSConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Redis2ApplicationTests {

    @Test
    void contextLoads() {
        OSSConfiguration ossConfiguration = new OSSConfiguration();
        System.out.println(ossConfiguration.getAccessKeyId());
        System.out.println(ossConfiguration.getAccessKeySecret());
        System.out.println(ossConfiguration.getBucketName());
        System.out.println(ossConfiguration.getEndpoint());
    }

}
