# 프로젝트 구조
본 프로젝트는 요구사항이 단순하고 명확한 도메인 모델보다는 CRUD 중심의 어플리케이션이며 프레임워크, 데이터베이스 등의 기술이 변경될 가능성이 낮기 때문에 DDD / 헥사고날 아키텍처 도입은 오버엔지니어링이라고 판단해 개층형 아키텍처를 선택했습니다.

| 항목          | 계층형 아키텍처                         | Domain-Driven Design                      | Hexagonal 아키텍처                              |
|------------|---------------------------------------|-------------------------------------------|-----------------------------------------------|
| 구조        | Controller → Service → Repository 계층 | 도메인 중심, 인프라 계층 분리                    | 내부(Core Domain)와 외부(어댑터) 인터페이스를 포트(Port)로 연결 |
| 의존성 방향   | 하향식                                  | 도메인이 애플리케이션/인프라 계층에 의존하지 않도록 설계 | 외부 어댑터가 내부 도메인에 의존 (의존성 역전)          |
| 외부 의존성   | 구현체(JPA 등)에 직접 의존                 | 인터페이스 추상화를 통한 간접 의존                | 포트와 어댑터를 통한 철저한 분리                      |
| 관심사 분리   | 기능별 수직적 계층 분리                     | 도메인 개념 기준의 역할 분리                    | 입출력과 도메인 로직을 수평적으로 분리                  |
| 복잡도       | 단순한 구조                              | 개념적 모델링, 계층 분리 작업 필요                | 도메인과 외부 인터페이스 분리로 인한 인터페이스 설계       |
| 유지보수      | 도메인 확장 시 계층 간 결합 증가             | 도메인 변경에 강하며 요구사항 변화에 유연           | 외부 의존성(JPA, Redis 등) 교체가 쉬움              |
| 테스트 용이성  | 계층 간 의존성 존재                        | 도메인 모델이 독립적이어서 단위 테스트 용이         | 포트-어댑터 분리로 단위/통합 테스트 모두 용이           |


```
product-service/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── shopping/
│   │   │           ├── ShoppingApplication.java
│   │   │           ├── controller/
│   │   │           │   ├── SearchController.java
│   │   │           │   ├── ProductController.java
│   │   │           ├── service/
│   │   │           │   ├── SearchService.java
│   │   │           │   └── ProductService.java
│   │   │           ├── repository/
│   │   │           │   ├── SearchRepository.java
│   │   │           │   ├── SearchRepositoryImpl.java
│   │   │           │   ├── ProductRepository.java
│   │   │           ├── model/
│   │   │           │   └── entity/
│   │   │           │       ├── BaseEntity.java
│   │   │           │       ├── Brand.java
│   │   │           │       └── Product.java
│   │   │           │   └── domain/
│   │   │           │       ├── BestBrandProduct.java
│   │   │           │       ├── BestPriceProduct.java
│   │   │           │       └── CategoryPriceProduct.java
│   │   │           │   └── dto/
│   │   │           │       ├── request/
│   │   │           │       │   ├── ProductCreateRequest.java
│   │   │           │       └── └── ProductUpdateRequest.java
│   │   │           │       ├── response/
│   │   │           │       │   ├── BestBrandResponse.java
│   │   │           │       │   ├── LowestProductsResponse.java
│   │   │           │       └── └── BestPriceResponse.java
│   │   │           ├── exception/
│   │   │           │   ├── GlobalExceptionHandler.java
│   │   │           │   └── GlobalExceptionResponse.java
│   │   │           ├── config/
│   │   │           │   ├── DatabaseConfig.java
│   │   │           │   ├── SwaggerConfig.java
│   │   │           │   └── WebConfig.java
│   │   │           └── util/
│   │   │               └── RegexpConstants.java 
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── data.sql
│   │       └── schema.sql
│   └── test/
│       └── java/
│           └── com/
│               └── shopping/
│                   ├── controller/
│                   │   ├── SearchControllerTest.java
│                   │   └── ProductControllerTest.java
│                   ├── service/
│                   │   ├── SearchServiceTest.java
│                   │   └── ProductServiceTest.java
│                   └── repository/
│                       ├── SearchRepositoryTest.java
│                       └── ProductRepositoryTest.java
└── build.gradle
```
