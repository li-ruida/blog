package com.lrdhappy.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author lrd
 * @date 2023-11-04 1:02
 */
@Service
@Slf4j
public class FixedWindowRateLimiter {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final int LIMIT = 5;

    private static final String FIX_WINDOW_KEY_PREFIX = "fixed-time-rate-limiter";
    private static final int TIME = 3;


    public boolean tryAcquire() {
        String key = getKey();
        long count = redisTemplate.opsForValue().increment(key, 1);
        if (count == 1) {
            redisTemplate.expire(key, TIME, TimeUnit.SECONDS);
        }
        return count <= LIMIT;
    }

    public String time(){
        DateTimeFormatter dtf=DateTimeFormatter.ofPattern("yyyy-MM-dd-hh:mm:ss");
        return dtf.format(LocalDateTime.now());
    }

    private String getKey() {
        return FIX_WINDOW_KEY_PREFIX;
    }
}
