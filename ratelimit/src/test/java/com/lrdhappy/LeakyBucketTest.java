package com.lrdhappy;

import com.lrdhappy.service.LeakyBucketRateLimiter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static java.lang.Thread.sleep;

/**
 * @author lrd
 * @date 2023-11-04 14:53
 */
@SpringBootTest
public class LeakyBucketTest {
    @Autowired
    LeakyBucketRateLimiter rateLimiter;
    @Test
    public void test(){
        for (int i = 0; i < 1000; i++) {
            try {
                if (rateLimiter.tryAcquire()){
                    System.out.println(rateLimiter.time() +" 处理");
//                    sleep(50);
                } else {
                    System.out.println(rateLimiter.time() + "丢弃");
//                    sleep(50);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
