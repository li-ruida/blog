package com.lrdhappy;

import com.google.common.util.concurrent.RateLimiter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.lang.Thread.sleep;

/**
 * @author lrd
 * @date 2023-11-04 17:16
 */
@SpringBootTest
public class TokenBucketTest {
    private static RateLimiter rateLimiter = RateLimiter.create(5);

    /**
     * 非阻塞
     */
    @Test
    public void test1(){
        for (int i = 0; i < 100; i++) {
            try {
                if (rateLimiter.tryAcquire()){
                    System.out.println(time() +" 处理");
                    sleep(150);
                } else {
                    System.out.println(time() + "丢弃");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    /**
     * 阻塞
     */
    @Test
    public void test2(){
        for (int i = 0; i < 100; i++) {
            try {
                rateLimiter.acquire();
                System.out.println(time() +" 处理");
                sleep(150);
            } catch (InterruptedException e) {
                System.out.println(time() +" 处理失败/丢弃");
                throw new RuntimeException(e);
            }
        }
    }
    public String time(){
        DateTimeFormatter dtf=DateTimeFormatter.ofPattern("yyyy-MM-dd-hh:mm:ss");
        return dtf.format(LocalDateTime.now());
    }
}
