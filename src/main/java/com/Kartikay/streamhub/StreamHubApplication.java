package com.kartikay.streamhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class StreamHubApplication {
    public static void main(String[] args) {
        SpringApplication.run(StreamHubApplication.class, args);
    }
}