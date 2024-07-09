package com.example.api.service;

import com.example.api.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApplyServiceTest {

    @Autowired
    private ApplyService applyService;

    @Autowired
    private CouponRepository couponRepository;

    @Test
    public void 한번만응모() {
        applyService.apply(1L);

        Long count = couponRepository.count();

        assertThat(count).isEqualTo(1L);
    }

    /**
     * Multi Thread 환경에서 쿠폰 발급 기능이 정상적으로 작동하는지 확인
     * ApplyService에서 설정한 "100개 이상의 쿠폰이 발급되지 않도록 하는" 로직이 제대로 작동하는지
     * 다중 사용자에 대해 동시에 쿠폰 발급 요청을 보냈을 때 동시성 문제 없이 최대 100개의 쿠폰만 발급되는지 확인
     */
    @Test
    public void 여러명응모() throws InterruptedException {
        // 1000개의 스레드 생성
        int threadCount = 1000;

        // ExecutorService는 병렬작업을 간단하게 도와주는 api
        // 32개의 고정된 Thread Pool 생성
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        // CountDownLatch는 다른 스레드에서 수행하는 작업을 기다리도록 도와주는 class
        // 1000개의 스레드가 작업을 완료할 때까지 대기하도록 설정
        CountDownLatch latch = new CountDownLatch(threadCount);

        // 1000개의 스레드가 각각 쿠폰 발급
        for (int i = 0; i < threadCount; i++) {
            long userId = i;
            executorService.submit(() -> {
                try {
                    applyService.apply(userId);
                } finally {
                    latch.countDown();
                }
            });
        }

        // 스레드가 작업을 마칠 때까지 대기
        latch.await();

        Thread.sleep(10000);

        // 저장된 쿠폰의 개수를 확인
        long count = couponRepository.count();

        // 저장된 쿠폰의 개수가 100인지 확인
        assertThat(count).isEqualTo(100);
    }

    @Test
    public void 한명당_한개의_쿠폰만_발급() throws InterruptedException {
        // 1000개의 스레드 생성
        int threadCount = 1000;

        // ExecutorService는 병렬작업을 간단하게 도와주는 api
        // 32개의 고정된 Thread Pool 생성
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        // CountDownLatch는 다른 스레드에서 수행하는 작업을 기다리도록 도와주는 class
        // 1000개의 스레드가 작업을 완료할 때까지 대기하도록 설정
        CountDownLatch latch = new CountDownLatch(threadCount);

        // 1000개의 스레드가 각각 쿠폰 발급
        for (int i = 0; i < threadCount; i++) {
            long userId = i;
            executorService.submit(() -> {
                try {
                    applyService.apply(1L);
                } finally {
                    latch.countDown();
                }
            });
        }

        // 스레드가 작업을 마칠 때까지 대기
        latch.await();

        Thread.sleep(10000);

        // 저장된 쿠폰의 개수를 확인
        long count = couponRepository.count();

        // 저장된 쿠폰의 개수가 100인지 확인
        assertThat(count).isEqualTo(1);
    }
}