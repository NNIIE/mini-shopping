# 요구 사항
다음 8개의 카테고리에서 상품을 하나씩 구매하여, 코디를 완성하는 서비스입니다.

- 상의, 아우터, 바지, 스니커즈, 가방, 모자, 양말, 액세서리

단, 구매 가격 외의 추가적인 비용(예, 배송비 등)은 고려하지 않고, 브랜드의 카테고리에는 1개의 상품은 존재하고, 구매를 고려하는 모든 쇼핑몰에 상품 품절은 없다고 가정하고 다음 4가지 기능을 구현해야 합니다.
1. 고객은 카테고리 별로 최저가격인 브랜드와 가격을 조회하고 총액이 얼마인지 확인할 수 있어야 합니다.
2. 고객은 단일 브랜드로 전체 카테고리 상품을 구매할 경우 최저가격인 브랜드와 총액이 얼마인지 확인할 수 있어야 합니다.
3. 고객은 특정 카테고리에서 최저가격 브랜드와 최고가격 브랜드를 확인하고 각 브랜드 상품의 가격을 확인할 수 있어야 합니다.
4. 운영자는 새로운 브랜드를 등록하고, 모든 브랜드의 상품을 추가, 변경, 삭제할 수 있어야 합니다.

<br>

# 실행 방법
- 

<br>

# 테스트 방법
- Unit Test
- Integration Test

<br>

# 개발 환경
### Language: Java 21 
- 더욱 최적화 된 G1 GC를 통한 낮은 GC 지연 시간
- JDK 8 대비 언어 기능과 성능 등에서 많은 향상이 있는 JDK 17의 모든 장점을 계승
- 최신 버전이라 일부 레거시 라이브러리 지원이 완벽하진 않지만 Spring, Hibernate, Gradle 등 주요 프레임워크/라이브러리는 완벽한 호환성 제공
  - ex) 바이트코드 조작 라이브러리, 구형 ORM 라이브러리 등
- 2031년 9월까지 유지보수 보장 (LTS)

| 항목          | JDK 8                            | JDK 17                               | JDK 21                                     |
|--------------|----------------------------------|--------------------------------------|--------------------------------------------|
| LTS 지원 기간      | 2030.12                           | 2029.09                             | 2031.09                                    |
| 주요 언어 특징  | - 람다<br>- Stream API<br>- Optional<br>- 인터페이스 Default 메서드 | - Record Class<br>- Sealed Class<br>- Switch 패턴 매칭(프리뷰)<br>- 텍스트 블록 | - Virtual Thread<br>- Scoped Values<br>-  Record 패턴 매칭<br>- String Templates (프리뷰) |
| 성능         | Parallel/CMS GC, C1/C2 Jit   | G1 GC / ZGC, C2 Jit | G1 GC 개선, ZGC 안정화         |
| 동시성 모델    | 전통적인 스레드              | - 개선된 ForkJoinPool<br>- CompletableFuture           | Virtual Thread 기반의 경량 스레드 모델 도입 |
| 라이브러리 | 안정적이며 풍부                  | 대다수 프레임워크 및 라이브러리 완벽 대응            | 주요 프레임워크는 완벽 대응, 일부 레거시 라이브러리 제한적 지원 |

### GC: G1 GC
- 이 어플리케이션은 전형적인 웹 어플리케이션의 특성을 가지고 있습니다. 따라서 처리량 중심의 Parallel GC보다는 처리량과 일시 정지 시간 사이의 균형을 잘 제공하며 Full GC 빈도가 낮아 안정적인 웹 어플리케이션의 운영에 적합한 G1 GC를 선택했습니다.

### Framework: Spring Boot 3.4.4
- JDK 21의 기능들을 지원하는 최신 정식 릴리즈 버전인 3.4.4를 선택했습니다.

### Database: MySQL 8.0
- 읽기 작업에 최적화된 MySQL을 선택했습니다. MySQL의 스토리지 엔진인 InnoDB에서는 클러스터드 인덱스로 인하여 순차적 읽기, 범위 검색같은 쿼리 패턴에 좋은 성능을 보이며 모든 보조인덱스가 PK를 포함하는 구조이기 때문에 커버링 인덱스 효과를 잘 얻을 수 있습니다. 또한 옵티마이저, 버퍼 풀 등에서 큰 성능향상이 있는 8.0 버전을 선택했습니다.

### 데이터 접근 기술: JPA + QueryDSL
- MyBatis, JDBC Template, JPA 중 객체와 테이블간의 매핑이 자동화 되어 엔티티 클래스의 설계가 데이터베이스 스키마로 자연스럽게 변환되고 반복적인 CRUD 쿼리를 자동으로 생성하여 생산성을 향상시키고 객체 지향 패러다임과 관계형 데이터베이스 간의 불일치 문제를 해결하는 ORM 프레임워크인 JPA를 선택했습니다. 또한 복잡한 동적 쿼리를 위해 컴파일 시점에 쿼리 오류를 검출할 수 있어 런타임 오류를 방지할 수 있는 QueryDSL을 선택했습니다.

### 테스트: JUnit 5 + Mockito
- 다양한 테스트 스타일을 유연하게 지원하며, 람다 표현식과 확장 모델 등 Java 21의 문법과도 잘 어울리는 JUnit 5를 선택했습니다.
- Mocking에는 Mockito를 활용하여 외부 의존성과 격리된 테스트가 가능하도록 했습니다. Mockito는 직관적인 API를 제공하고 Spring Boot와의 통합도 잘 되어 있어 생산성과 유지보수성을 동시에 고려한 선택입니다.

### Build Tool: Gradle
- Maven 과 비교하여 Groovy/Kotlin DSL을 통한 선언적 방식으로 더 간결한 표현력 있는 빌드 스크립트 작성이 가능하고 증분 빌드 기능으로 변경된 부분만 재컴파일하여 빌드 시간을 크게 단축시킬 수 있는 Gradle을 선택했습니다.

### 실행 환경: Docker
- 어플리케이션을 컨테이너화하여 일관된 환경을 보장하고 환경구성에 관계없이 동일하게 실행될 수 있는 Docker를 선택했습니다.

### 문서화: SpringDoc(OpenAPI 3)
- Spring Boot 3 대응이 미흡한 SpringFox 대신 OpenAPI 3 스펙을 기반으로 하고 있으며 Spring Boot 3와의 호환성과 지원이 뛰어난 SpringDoc을 선택했습니다.

<br>

# 프로젝트 구조
요구사항이 복잡하지 않아 최대한 단순하고 파악하기 쉬운 모놀리식 아키텍처 (Controller-Service-Repository) 로 설계했습니다.
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

<br>

# API
### 상품
|    METHOD   | URL |  기능                 |
|----------|--------|----------------------|
| GET      | /product | 상품 목록 조회        |
| GET      | /product/{id} | 특정 상품 조회   |
| POST     | /product | 상품 등록            |
| PATCH    | /product/{id} | 상품 수정       |
| DELETE   | /product/{id} | 상품 삭제       |

### 검색
| METHOD       | URL | 기능                  |
|----------|--------|----------------------|
| GET      | /search/categories/lowest-prices | 카테고리 별 최저가 브랜드의 상품가격 및 총액 조회 |
| GET      | /search/brands/best-value | 단일 브랜드로 모든 카테고리 구매시 최저가격 브랜드와 가격, 총액 조회 |
| GET      | /search/category/{category}/price-range | 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격 조회 |

