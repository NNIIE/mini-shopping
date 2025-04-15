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
- 최적화된 G1 GC를 통해 예측 가능한 낮은 GC 시간 을 제공하며 안정성과 처리량의 균형을 효과적으로 맞출 수 있습니다.
- JDK 8 대비 언어 기능과 성능 측면에서 크게 향상된 JDK 17의 기능들을 모두 계승하고 있습니다.
- 가장 장기간 유지보수가 지원됩니다.
- 최신 버전이기 때문에 일부 레거시 라이브러리(예: 바이트코드 조작 라이브러리, 구형 ORM 등) 와의 호환성 이슈가 존재할 수 있으나 신규 프로젝트 이고 Spring, Hibernate, Gradle 등 주요 프레임워크 및 라이브러리와는 완전한 호환성을 제공하며 더욱 향상된 G1 GC 기능을 사용하고자 JDK 21로 선택했습니다.

| 항목          | JDK 8                            | JDK 17                               | JDK 21                                     |
|--------------|----------------------------------|--------------------------------------|--------------------------------------------|
| LTS 지원 기간  | 2030.12                          | 2029.09                              | 2031.09                                    |
| 주요 언어 특징  | - 람다<br>- Stream API<br>- Optional<br>- 인터페이스 Default 메서드 | - Record Class<br>- Sealed Class<br>- Switch 패턴 매칭(프리뷰)<br>- 텍스트 블록 | - Virtual Thread<br>- Scoped Values<br>-  Record 패턴 매칭<br>- String Templates (프리뷰) |
| 성능          | Parallel/CMS GC, C1/C2 Jit       | G1 GC / ZGC, C2 Jit | G1 GC 개선, ZGC 안정화         |
| 동시성 모델     | 전통적인 스레드 기반                  | - 개선된 ForkJoinPool<br>- CompletableFuture 강화    | Virtual Thread 기반의 경량 스레드 모델 도입 |
| 라이브러리 | 안정적이며 풍부                  | 대다수 프레임워크 및 라이브러리 완벽 대응            | 주요 프레임워크 완벽 대응, 일부 레거시 라이브러리는 제한 가능성 있음 |

<br>

### GC: G1 GC
- G1 GC는 Region 기반의 설계로 전체 힙을 대상으로 수집하지 않고 우선순위가 높은 Region만 선택적으로 수집하기 때문에 GC 시간이 짧게 유지됩니다.
- `-XX:MaxGCPauseMillis` 옵션을 활용하면 다양한 트래픽 패턴에서도 예측 가능한 GC 시간을 유지할 수 있습니다.
- ZGC도 검토하였으나 저지연 응답 시간에는 적합하지만 처리량 및 라이브러리 호환성 측면에서 아쉬움이 존재하고 웹 어플리케이션에서는 응답 시간과 처리량의 균형이 중요하다고 판단해 G1 GC를 선택했습니다.

| 항목            | Parallel GC                            | G1 GC                               | ZGC                                   |
|----------------|----------------------------------|--------------------------------------|--------------------------------------------|
| 기본 목표        | 높은 처리량 (Throughput)            | 예측 가능한 지연 시간 + 준수한 처리량         | 저지연 응답 시간                               |
| 기본 전략        | 모든 영역 병렬 수집 <br>(Young + Old 병렬 수집) | Region 기반 Young - Mixed GC  | Colored Pointer + Load/Store Barrier 기반의 동시 수집  |
| Full GC 빈도    | 빈번함 (Old 영역이 차면 발생) | 드물게 발생 (Humongous 객체 누적, Region 단편화, Mixed GC 실패 시) | 거의 발생하지 않음 (동시 수집 메모리 설계상 회피) |
| Stop-the-world | 수백ms ~ 수초 (힙 크기에 비례)         | 수십 ~ 수백ms (목표 설정 가능)             | < 1ms (힙 크기와 무관)                         |
| 처리량           | 매우 높음                          | 높음                                   |        중간                                  |
| 안정성 (Java 21) | 매우 안정적                         | 매우 안정적 + Mixed GC 압축 개선           | 안정적 (최근 안정화)                            |
| 최적 환경 | 배치 서비스 등의 응답 시간 중요하지 않은 환경 | 웹, API 서버 – 일관된 응답시간 + 적절한 Throughput 요구 환경 | 실시간 분석, 거래소, 게임 서버같은 저지연 환경이 요구되는 환경 |

<br>

### Framework: Spring Boot 3.4.4
- 본 프로젝트는 신규 개발이므로 처음부터 `jakarta.*` 기준으로 구성할 수 있습니다.
- JDK 21에서 도입된 최신 언어 및 플랫폼 기능은 Spring Boot 3.x 이상에서만 공식 지원됩니다.
- 이전 버전에 의존하는 레거시 요소가 없기 때문에 JDK 21의 기능들을 지원하는 최신 정식 릴리즈 버전인 3.4.4 버전을 선택했습니다.

| 항목          | Spring Boot 2.X               | Spring Boot 3.X                         |
|----------------|-------------------------------|--------------------------------------|
| JDK 호환        | 8-17 지원                      | 17 이상만 지원                          |
| 기반 프레임워크    | Spring Framework 5.x          | Spring Framework 6.x                 |
| API 네임스페이스   | `javax.*`                      | `jakarta.*`                        | 
| 네이티브 이미지    | GraalVM 제한적 지원              | GraalVM 네이티브 이미지 정식 지원          | 
| Virtual Thread | 미지원                          | 완벽 지원 (Thread Pool 대체 가능)         | 
| 라이브러리        | `javax.*` 기반의 외부 라이브러리와 호환성이 높음          |  `javax.*` 기반의 일부 라이브러리에서 호환성 이슈 발생 가능   | 

<br>

### Database: MySQL 8.0
- PostgreSQL과 Oracle은 복잡한 다중 조인, 서브쿼리, 분석 함수 등의 처리에서 MySQL보다 옵티마이저의 통계 기반 정밀도 및 실행 계획 다양성을 제공하지만 InnoDB 스토리지 엔진은 PK 기반 클러스터드 인덱스로 인접한 레코드를 디스크에서 물리적 근접성을 유지하여 범위 검색에 효율이 좋고 모든 보조 인덱스는 PK를 포함하므로 커버링 인덱스를 활용하기 쉽습니다.
- InnoDB의 MVCC는 undo 로그에 기반한 트랜잭션 스냅샷 방식으로 동작하며 PostgreSQL의 autovacuum이나 Oracle의 undo retention 정책과 같은 운영 부담 없이도 안정적인 동시성 처리가 가능합니다.
- 본 프로젝트는 비교적 단순한 조인 구조와 단건/범위 조회 패턴이 많고 복잡한 트랜잭션 경합이 적기 때문에 클러스터드 인덱스와 커버링인덱스로 인한 이점이 더 크다고 판단했고 동시성 문제를 MVCC로 해결할 수 있을거라고 판단해 MySQL을 선택했습니다.

| 항목          | MySQL                            | PostgreSQL                               | Oracle                                 |
|--------------|----------------------------------|--------------------------------------|--------------------------------------------|
| 스토리지       | InnoDB (PK 기반 클러스터드 인덱스 구조) | Heap 기반 테이블 구조                    | 계층적 Segment 기반 저장 구조                   |
| 인덱스 타입     | B+Tree, 전문검색, 공간인덱스 등        | B-Tree, GIN, GiST 등                 | B+Tree, 비트맵, 도메인 등                      |
| 클러스터드 인덱스 | 기본 지원 (PK 기반 클러스터드 인덱스 + 보조 인덱스) | 기본 미지원 (CLUSTER 명령으로 일회성 클러스터링만 가능) | IOT(Index-Organized Tables)를 통한 선택적 구현 |
| 보조 인덱스     | PK 참조  (PK 크기가 보조인덱스 크기에 영향을 미침)   | TID(Heap Tuple ID)를 참조해 원본 행 조회   | ROWID 기반으로 원본 레코드 조회        |
| 커버링 인덱스    | 강력 지원 (보조 인덱스에 PK 포함)       | 제한적 (include 구문 필요)              | 지원 (Index Full Scan)                       |
| 옵티마이저      | 비용 기반(제한적 통계수집, 히스토그램) | 고급 비용 기반(통계도 다변량, 히스토그램, 조인 재정렬, 인덱스 선택) | 고도화 된 비용 기반(실시간 통계 수집 + SQL Plan Baseline, 적응형 실행 계획) |
| MVCC         | - 언두 로그 기반 <br>- 트랜잭션 단위 스냅샷 <br>- Vacuum 불필요 <br>- 언두 로그만 읽음   | - 튜플 버전 기반 <br>-쿼리 단위 스냅샷 <br>- Vacuum 필요 <br>- 튜플마다 visible 여부 판별    | - SCN 기반 <br>- 트랜잭션 단위 스냅샷 <br>- undo retention, 공간 관리 필요 <br>- SCN 일치 여부 및 undo scan 필요      |

<br>

### 데이터 접근 기술: JPA + QueryDSL
- MyBatis, JDBC Template, JPA 중 객체와 테이블간의 매핑이 자동화 되어 엔티티 클래스의 설계가 데이터베이스 스키마로 자연스럽게 변환되고 반복적인 CRUD 쿼리를 자동으로 생성하여 생산성을 향상시키고 객체 지향 패러다임과 관계형 데이터베이스 간의 불일치 문제를 해결하는 ORM 프레임워크인 JPA를 선택했습니다. 또한 복잡한 동적 쿼리를 위해 컴파일 시점에 쿼리 오류를 검출할 수 있어 런타임 오류를 방지할 수 있는 QueryDSL을 선택했습니다.

<br>

### 테스트: JUnit 5 + Mockito
- 다양한 테스트 스타일을 유연하게 지원하며, 람다 표현식과 확장 모델 등 Java 21의 문법과도 잘 어울리는 JUnit 5를 선택했습니다.
- Mocking에는 Mockito를 활용하여 외부 의존성과 격리된 테스트가 가능하도록 했습니다. Mockito는 직관적인 API를 제공하고 Spring Boot와의 통합도 잘 되어 있어 생산성과 유지보수성을 동시에 고려한 선택입니다.

<br>

### Build Tool: Gradle
- Maven 과 비교하여 Groovy/Kotlin DSL을 통한 선언적 방식으로 더 간결한 표현력 있는 빌드 스크립트 작성이 가능하고 증분 빌드 기능으로 변경된 부분만 재컴파일하여 빌드 시간을 크게 단축시킬 수 있는 Gradle을 선택했습니다.

<br>

### 실행 환경: Docker
- 어플리케이션을 컨테이너화하여 일관된 환경을 보장하고 환경구성에 관계없이 동일하게 실행될 수 있는 Docker를 선택했습니다.

<br>

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

