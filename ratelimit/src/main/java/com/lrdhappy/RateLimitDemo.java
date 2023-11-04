package com.lrdhappy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lrd
 * @date 2023-11-04 0:30
 */
@SpringBootApplication(scanBasePackages="com.lrdhappy")
public class RateLimitDemo {
    public static void main(String[] args) {
        SpringApplication.run(RateLimitDemo.class, args);
    }
}