package com.lrdhappy.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * <p>LeakyBucketRateLimit</p>
 *
 * @author wangjn
 * @since 2019-10-08
 */
@Slf4j
@Service
public class LeakyBucketRateLimiter{
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final long BUCKET_RATE = 10; // 令牌生成速率（令牌/秒）

    private static final String BUCKET_KEY_PREFIX = "leaky-bucket-rate-limiter";
    private static final long BUCKET_CAPACITY = 100L;

    public boolean tryAcquire() {
        long currentTime = System.currentTimeMillis();
        String currentKey = String.valueOf(currentTime);
        // 尝试从令牌桶中获取一个令牌
        Long tokenCount = count();
        if (tokenCount < BUCKET_CAPACITY) {
            // 令牌桶未满，添加一个令牌
            redisTemplate.opsForZSet().add(BUCKET_KEY_PREFIX, currentKey, currentTime);
            //令牌桶数量变化
            return count()!=tokenCount;
        } else {
            // 令牌桶已满，移除过期的令牌
            long removeCount = redisTemplate.opsForZSet().removeRangeByScore(BUCKET_KEY_PREFIX, 0, currentTime - 1000);
            if (removeCount < BUCKET_RATE) {
                // 未移除足够数量的令牌，请求被限流
                return false;
            } else {
                // 移除了足够数量的令牌，添加新令牌
                redisTemplate.opsForZSet().add(BUCKET_KEY_PREFIX, currentKey, currentTime);
                return true;
            }
        }
    }

    public String time(){
        DateTimeFormatter dtf=DateTimeFormatter.ofPattern("yyyy-MM-dd-hh:mm:ss");
        return dtf.format(LocalDateTime.now());
    }

    private String getKey() {
        return BUCKET_KEY_PREFIX;
    }

    private long count(){
        return  redisTemplate.opsForZSet().zCard(BUCKET_KEY_PREFIX);
    }
}