package com.lrdhappy;

import com.lrdhappy.service.FixedWindowRateLimiter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static java.lang.Thread.sleep;

/**
 * @author lrd
 * @date 2023-11-04 13:45
 */
@SpringBootTest
public class FixWindowTest {
    @Autowired
    FixedWindowRateLimiter fixedWindowRateLimiter;
    @Test
    public void test(){
        for (int i = 0; i < 1000; i++) {
            try {
                if (fixedWindowRateLimiter.tryAcquire()){
                    System.out.println(fixedWindowRateLimiter.time() +" 处理");
                    sleep(123);
                } else {
                    System.out.println(fixedWindowRateLimiter.time() + "丢弃");
                    sleep(25);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
