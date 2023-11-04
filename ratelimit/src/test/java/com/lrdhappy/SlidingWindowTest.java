package com.lrdhappy;

import com.lrdhappy.service.SlidingWindowRateLimiter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static java.lang.Thread.sleep;

/**
 * @author lrd
 * @date 2023-11-04 14:18
 */

@SpringBootTest
public class SlidingWindowTest {
    @Autowired
    SlidingWindowRateLimiter rateLimiter;
    @Test
    public void test(){
        for (int i = 0; i < 100; i++) {
            try {
                if (rateLimiter.tryAcquire()){
                    System.out.println(rateLimiter.time() +" 处理");
                    sleep(150);
                } else {
                    System.out.println(rateLimiter.time() + "丢弃");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
