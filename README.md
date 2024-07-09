# 선착순 이벤트 시스템

## 1. 개요

### 설명

- 선착순으로 쿠폰을 발급하는 이벤트 시스템
- 선착순 이벤트 시스템을 단계적으로 만들어 나가면서, 각 단계에서 어떠한 문제점이 발생하고 이를 어떻게 해결하는지를 학습
- Docker 환경에서 Redis, Kafka를 구성하며 프로젝트에서 주로 사용하는 명령어를 실습

### 사용 기슬

- Java
- Spring Boot
- Spring Data JPA
- Redis
- Apache Kafka
- MySQL
- Docker

### 참고 강의

- [실습으로 배우는 선착순 이벤트 시스템 - 최상용](https://www.inflearn.com/course/%EC%84%A0%EC%B0%A9%EC%88%9C-%EC%9D%B4%EB%B2%A4%ED%8A%B8-%EC%8B%9C%EC%8A%A4%ED%85%9C-%EC%8B%A4%EC%8A%B5#)

---

## 2. 프로젝트 구조

```shell
coupon-system
│  .gitignore
│  build.gradle
│  docker-compose.yml
│  gradlew
│  gradlew.bat
│  README.md
│  settings.gradle
├─.gradle ...
├─.idea ...
│
├─api
│  │  .gitignore
│  │  build.gradle
│  │  gradlew
│  │  gradlew.bat
│  │  settings.gradle
│  ├─.gradle ...
│  ├─build ...
│  ├─gradle ...
│  │
│  └─src
│      ├─main
│      │  ├─java
│      │  │  └─com
│      │  │      └─example
│      │  │          └─api
│      │  │              │  ApiApplication.java
│      │  │              ├─config
│      │  │              │      KafkaProducerConfig.java
│      │  │              ├─domain
│      │  │              │      Coupon.java
│      │  │              ├─producer
│      │  │              │      CouponCreateProducer.java
│      │  │              ├─repository
│      │  │              │      AppliedUserRepository.java
│      │  │              │      CouponCountRepository.java
│      │  │              │      CouponRepository.java
│      │  │              └─service
│      │  │                      ApplyService.java
│      │  │
│      │  └─resources
│      │          application.yml
│      │
│      └─test
│          └─java
│              └─com
│                  └─example
│                      └─api
│                          │  ApiApplicationTests.java
│                          └─service
│                                  ApplyServiceTest.java
│
├─consumer
│  │  .gitignore
│  │  build.gradle
│  │  consumer.iml
│  │  gradlew
│  │  gradlew.bat
│  │  settings.gradle
│  ├─.gradle ...
│  ├─build ...
│  ├─gradle ...
│  │
│  └─src
│      ├─main
│      │  ├─java
│      │  │  └─com
│      │  │      └─example
│      │  │          └─consumer
│      │  │              │  ConsumerApplication.java
│      │  │              ├─comsumer
│      │  │              │      CouponCreatedConsumer.java
│      │  │              ├─config
│      │  │              │      KafkaConsumerConfig.java
│      │  │              ├─domain
│      │  │              │      Coupon.java
│      │  │              │      FailedEvent.java
│      │  │              └─repository
│      │  │                      CouponRepository.java
│      │  │                      FailedEventRepository.java
│      │  │
│      │  └─resources
│      │          application.yml
│      │
│      └─test
│          └─java
│              └─com
│                  └─example
│                      └─consumer
│                              ConsumerApplicationTests.java
│
└─gradle ...
```

---

## 3. 기능별 설명

### Docker 구조

![docker.png](docker.png)

---

## 4. 주요 기능

