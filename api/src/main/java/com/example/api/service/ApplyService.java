package com.example.api.service;

import com.example.api.domain.Coupon;
import com.example.api.producer.CouponCreateProducer;
import com.example.api.repository.AppliedUserRepository;
import com.example.api.repository.CouponCountRepository;
import com.example.api.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class ApplyService {

    private final CouponRepository couponRepository;
    private final CouponCountRepository couponCountRepository;
    private final CouponCreateProducer couponCreateProducer;
    private final AppliedUserRepository appliedUserRepository;

    public ApplyService(CouponRepository couponRepository, CouponCountRepository couponCountRepository, CouponCreateProducer couponCreateProducer, AppliedUserRepository appliedUserRepository) {
        this.couponRepository = couponRepository;
        this.couponCountRepository = couponCountRepository;
        this.couponCreateProducer = couponCreateProducer;
        this.appliedUserRepository = appliedUserRepository;
    }

    /**
     * 쿠폰 발급
     */
    public void apply(Long userId) {
        // userId에 대한 쿠폰 중복 발급 여부 확인
        if (appliedUserRepository.add(userId) != 1) {
            return;
        }

        // 쿠폰의 잔여 갯수를 통해 발급 가능 여부 확인
        if (couponCountRepository.increment() > 100) {
            return;
        }

        // Kafka Topic(coupon_create)으로 userId 발행
        couponCreateProducer.create(userId);
    }
}
