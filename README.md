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
최신 버전이기 때문에 일부 레거시 라이브러리(예: 바이트코드 조작 라이브러리, 구형 ORM 등) 와의 호환성 이슈가 존재할 수 있으나 신규 프로젝트 이고 Spring, Hibernate, Gradle 등 주요 프레임워크 및 라이브러리와는 완전한 호환성을 제공하며 더욱 향상된 G1 GC 기능을 사용하고자 JDK 21로 선택했습니다.
- 최적화된 G1 GC를 통해 예측 가능한 낮은 GC 시간 을 제공하며 안정성과 처리량의 균형을 효과적으로 맞출 수 있습니다.
- JDK 8 대비 언어 기능과 성능 측면에서 크게 향상된 JDK 17의 기능들을 모두 계승하고 있습니다.
- 가장 장기간 유지보수가 지원됩니다.

| 항목          | JDK 8                            | JDK 17                               | JDK 21                                     |
|--------------|----------------------------------|--------------------------------------|--------------------------------------------|
| LTS 지원 기간  | 2030.12                          | 2029.09                              | 2031.09                                    |
| 주요 언어 특징  | - 람다<br>- Stream API<br>- Optional<br>- 인터페이스 Default 메서드 | - Record Class<br>- Sealed Class<br>- Switch 패턴 매칭(프리뷰)<br>- 텍스트 블록 | - Virtual Thread<br>- Scoped Values<br>-  Record 패턴 매칭<br>- String Templates (프리뷰) |
| 성능          | Parallel/CMS GC, C1/C2 Jit       | G1 GC / ZGC, C2 Jit | G1 GC 개선, ZGC 안정화         |
| 동시성 모델     | 전통적인 스레드 기반                  | - 개선된 ForkJoinPool<br>- CompletableFuture 강화    | Virtual Thread 기반의 경량 스레드 모델 도입 |
| 라이브러리 | 안정적이며 풍부                  | 대다수 프레임워크 및 라이브러리 완벽 대응            | 주요 프레임워크 완벽 대응, 일부 레거시 라이브러리는 제한 가능성 있음 |

<br>

### GC: G1 GC
ZGC도 검토하였으나 저지연 응답 시간에는 적합하지만 처리량 및 라이브러리 호환성 측면에서 아쉬움이 존재하고 웹 어플리케이션에서는 응답 시간과 처리량의 균형이 중요하다고 판단해 G1 GC를 선택했습니다.
- G1 GC는 Region 기반의 설계로 전체 힙을 대상으로 수집하지 않고 우선순위가 높은 Region만 선택적으로 수집하기 때문에 GC 시간이 짧게 유지됩니다.
- `-XX:MaxGCPauseMillis` 옵션을 활용하면 다양한 트래픽 패턴에서도 예측 가능한 GC 시간을 유지할 수 있습니다.

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
이전 버전에 의존하는 레거시 요소가 없기 때문에 JDK 21의 기능들을 지원하는 최신 정식 릴리즈 버전인 3.4.4 버전을 선택했습니다.
- 본 프로젝트는 신규 개발이므로 처음부터 `jakarta.*` 기준으로 구성할 수 있습니다.
- JDK 21에서 도입된 최신 언어 및 플랫폼 기능은 Spring Boot 3.x 이상에서만 공식 지원됩니다.

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
본 프로젝트는 비교적 단순한 조인 구조와 단건/범위 조회 패턴이 많고 복잡한 트랜잭션 경합이 적기 때문에 클러스터드 인덱스와 커버링인덱스로 인한 이점이 더 크다고 판단했고 동시성 문제를 MVCC로 해결할 수 있을거라고 판단해 MySQL을 선택했습니다.
- PostgreSQL과 Oracle은 복잡한 다중 조인, 서브쿼리, 분석 함수 등의 처리에서 MySQL보다 옵티마이저의 통계 기반 정밀도 및 실행 계획 다양성을 제공하지만 InnoDB 스토리지 엔진은 PK 기반 클러스터드 인덱스로 인접한 레코드를 디스크에서 물리적 근접성을 유지하여 범위 검색에 효율이 좋고 모든 보조 인덱스는 PK를 포함하므로 커버링 인덱스를 활용하기 쉽습니다.
- InnoDB의 MVCC는 undo 로그에 기반한 트랜잭션 스냅샷 방식으로 동작하며 PostgreSQL의 autovacuum이나 Oracle의 undo retention 정책과 같은 운영 부담 없이도 안정적인 동시성 처리가 가능합니다.

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

### 데이터 접근 기술: JPA
본 프로젝트는 현재 복잡한 동적 쿼리나 대량 데이터 처리가 요구되지 않으며 이는 기술 선택 시 중요한 고려 요소였습니다. 향후 대량 데이터를 처리해야 하는 상황이 발생하더라도 JPA의 한계를 보완할 수 있는 `JdbcTemplate` 또는 `native query` 를 사용할 수 있기 때문에 엔티티 클래스와 테이블 간의 매핑 자동화로 도메인 모델과 데이터베이스 스키마가 일치하도록 구성할 수 있고 반복적인 CRUD 쿼리를 자동 생성 자동생성함으로써 개발 생산성을 향상시킬 수 있는 ORM 프레임워크 JPA를 선택했습니다.

| 항목          | JPA                            | MyBatis                               | JDBC Template                              |
|--------------|--------------------------------|---------------------------------------|--------------------------------------------|
| 단순 CRUD     | 자동 생성                        | SQL 작성 필요                            | SQL 작성 필요                                |
| 동적 쿼리      | JPQL, QueryDSL 등 필요           | XML, 어노테이션 기반 지원                  | 문자열 직접 조합 필요                           |
| 객체-테이블 매핑 | 자동 (Entity = Table)           | 수동 매핑                                | 수동 매핑                                    |
| 생산성         | 단순 CRUD 자동                   | CRUD의 반복 작성이 필요하지만 SQL 유연함       | 모든 SQL과 매핑 수동 구현                      |
| 성능 튜닝       | 2차 캐시, Fetch 전략, 영속성 컨텍스트 등 ORM 전용 최적화 가능  | SQL 튜닝 자유롭지만 연관관계 최적화, 캐싱 수동 | SQL 단위 튜닝            |
| 대량 데이터 처리 | persist 반복 + flush/clear 필요  | foreach + <insert> 태그로 처리 가능         | batchUpdate()로 처리 가능                   |

<br>

### 동적 쿼리 기술: QueryDSL
QueryDSL은 Q Class 생성이라는 번거로움과 빌드 시 apt 설정, 멀티모듈 환경에서의 Q타입 공유 이슈 등의 단점이 존재하지만 본 프로젝트는 단일 모듈 구조로 Q타입 공유 부담이 없기 때문에 컴파일 시점 타입 안전성, 직관적인 API, 높은 가독성과 유지보수성이라는 장점들이 더 큰 가치가 있다고 판단해 QueryDSL로 선택했습니다.
- QueryDSL은 타입 안전하게 쿼리를 작성할 수 있어 컴파일 시점에 오류를 검출할 수 있으며 필드명 변경 시 자동으로 감지되어 리팩토링에 강점이 있습니다
- 메서드 체이닝 방식의 유연한 API로 복잡한 동적 쿼리를 가독성 높게 작성할 수 있으며 재사용 가능한 쿼리 조각을 분리하여 관리할 수 있습니다
- JPQL은 타입 안정성은 없지만 텍스트 블록을 활용한 문자열 조합으로 직관적이며 유지보수성이 좋고 빌드 시 추가 설정이 필요 없습니다

| 항목          | QueryDSL                            | JPQL                               | Criteria API                             |
|--------------|--------------------------------|---------------------------------------|--------------------------------------------|
| 타입 안정성     | 컴파일 시점 쿼리 오류 검출, 도메인 필드명 변경 자동 감지   | 문자열 기반으로 런타임 오류 발생 가능성 존재  | 컴파일 시점 체크                   |
| 빌드          | Q 클래스 생성을 위한 Annotation Processing Tool 설정 필요   | 별도 설정 없음             | 별도 설정 없음                         |
| Q Class      | 빌드시마다 Q 클래스 생성, 멀티모듈 시 공유 및 의존성 문제 발생  | 없음                         | 없음                                |
| 가독성         | 메서드 체이닝으로 유연하고 직관적 | 문자열 조합이지만 텍스트 블록을 활용하면 직관적            | Builder 패턴 중첩 많고 비교적 장황함           |
| 유지보수        | 필드 변경 시 Q타입 자동 반영, 조건 추출 및 재사용 용이 | 문자열 직접 수정이지만 단순쿼리 유지 용이 | 복잡한 구조때문에 유지보수 비용 높음          |

<br>

### 테스트: JUnit 5 + Mockito
Spock의 BDD 스타일, 강력한 파라미터화, 자체내장 Mocking은 매력적이었지만 Java 21의 최신 기능을 완벽하게 지원하고 Spring Boot와의 통합이 원활하며 풍부한 커뮤니티 자원과 레퍼런스를 제공한다는 점을 고려하여 JUnit 5를 선택했습니다. Mocking 라이브러리로는 직관적인 API, Spring의 @MockBean 통합, 활발한 커뮤니티 지원을 고려하여 Mockito를 선택했습니다.


| 항목          | JUnit 5                        | TestNG                                | Spock                                      |
|--------------|--------------------------------|---------------------------------------|--------------------------------------------|
| 기반 언어      | Java                           | Java                                  | Groovy                                     |
| 문법 스타일     | Java 표준, 람다/어노테이션 기반      | Java 표준, XML 설정                     | Groovy 기반의 BDD 스타일                      |
| Java 21 호환  | 호환                            | 호환                                   |  Groovy 기반으로 Java 최신 문법 제한             |
| Spring boot 호환 | Spring Boot 3.x 공식 지원     | 추가 설정 필요                            | 플러그인 필요                                 |
| Mocking      | 외부 라이브러리 필요                | 외부 라이브러리 필요                       | 자체 내장 Mocking DSL                        |
| 병렬 실행      | 선언적 설정 (junit-platform.properties) | parallel 속성, 설정 유연            | 제한적 (Groovy에서 병렬 처리 제약)              |
| Maven/Gradle 통합 | 기본 지원                    | 기본 지원                                | 플러그인 필요                                |
| 동적 테스트/파라미터화 | 지원 (@ParameterizedTest, @MethodSource 등) | 제한적 제공 (@DataProvider) | 기본 내장 지원                             |
| 실행 순서 제어   | @TestMethodOrder, @Order로 선언적 지원  | priority, dependsOnMethods 지원   | 명시적 실행 순서 제어 어려움                    |

| 항목          | Mockito                        | EasyMock                              | JMockit                                    |
|--------------|--------------------------------|---------------------------------------|--------------------------------------------|
| Mocking 방식  | 런타임 Proxy                     | Record → Replay 패턴 기반 프록시          | 런타임 바이트코드 조작                          |
| BDD 지원      | 지원 (given().willReturn())     | X                                     | 구조 자체가 BDD 기반                           |
| 개발 지원      | 활발한 개발, 커뮤니티               | 제한적 업데이트                           | 제한적 업데이트                                |
| Spring 통합   | @MockBean, @ExtendWith 등 공식 지원 | 직접 DI 필요                          | 제한적, 직접 설정 필요                          |
| JUnit 5 통합  | MockitoExtension으로 공식 지원     | 수동 설정 필요                           | 수동 설정 필요                                |
| TestNG 통합   | 수동 설정 필요                     | 수동 설정 필요                           | 수동 설정 필요                                |
| Static/Final Mocking | 3.4+부터 공식 지원        | X                                     |  지원                                       |

<br>

### Build Tool: Gradle
Maven도 안정적이고 널리 사용되는 빌드 도구이지만 본 프로젝트에서는 증분 빌드 및 빌드 캐시 기능을 활용하여 빌드 시간을 크게 단축시키고 Groovy/Kotlin DSL 기반의 유연한 빌드 스크립트 작성이 가능한 Gradle을 선택했습니다.

| 항목            | Gradle                        | Maven                                |
|----------------|-------------------------------|--------------------------------------|
| 문법            | Groovy, Kotlin DSL            | XML                                  |
| 증분 빌드        | 변경된 파일만 재컴파일              | 모듈 단위 빌드만 가능                     |
| 빌드 캐시        | 로컬 및 원격 캐시 제공             | 로컬 리포지토리 기반의 제한적 캐싱            | 
| 멀티 모듈 관리    | 유연한 구성과 설정 공유, 커스텀 의존성 로직 구현 용이 | 표준화된 접근 방식, POM 상속 구조로 다소 제한적 | 
| 플러그인 생태계    | 풍부하지만 비교적 최근             | 풍부하고 성숙함                           | 
| Java 21 지원    | 공식 지원                       | 공식 지원                               | 
| Spring 호환     | 공식 지원                       |  공식 지원                              | 

<br>

# 프로젝트 구조
본 프로젝트는 요구사항이 복잡하지 않고 명확한 도메인 규칙보다는 단순한 CRUD 중심이기 때문에 구조적으로 가장 단순하면서도 빠른 개발이 가능한 계층형 아키텍처를 선택했습니다. 향후 도메인 복잡도가 증가하거나 외부 시스템과의 인터페이스가 다양해질 경우 DDD나 Hexagonal 아키텍처로의 점진적인 구조 전환을 고려할 수 있습니다.

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

