package com.lmk.redis2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
public class Redis2Application {

    public static void main(String[] args) {
        SpringApplication.run(Redis2Application.class, args);
    }

}
